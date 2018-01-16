/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.evoppi.service.bio;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusDegreeFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusDegreeFinishedEvent.GeneInteraction;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusDegreeStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.entity.bio.InteractingGenes;
import org.sing_group.evoppi.service.entity.bio.InteractionGroup;

@Stateless
@PermitAll
public class DefaultSameSpeciesInteractionService implements SameSpeciesInteractionService {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private Event<SameSpeciesCalculusStartedEvent> startEvents;
  
  @Inject
  private Event<SameSpeciesCalculusDegreeStartedEvent> startDegreeEvents;
  
  @Inject
  private Event<SameSpeciesCalculusDegreeFinishedEvent> finishDegreeEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFinishedEvent> finishEvents;
  
  @Override
  @Asynchronous
  public void calculateSameSpeciesInteractions(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) SameSpeciesInteractionsRequestEvent event
  ) {
    notifyCalculusStarted(event);
    
    final Gene gene = this.geneDao.getGene(event.getGeneId());
    final Set<Integer> interactomesIds = IntStream.of(event.getInteractomes())
      .boxed()
    .collect(toSet());
    
    final InteractionGroupsAccumulator accumulator = new InteractionGroupsAccumulator();

    Set<Gene> genes = singleton(gene);
    Set<Interaction> interactions = new HashSet<>();
    for (int degree = 1; degree <= event.getMaxDegree(); degree++) {
      final int currentDegree = degree;
      notifyDegreeCalculusStarted(event, currentDegree);
      
      accumulator.addGroups(calculateInteractions(
        degree,
        genes,
        interaction ->
          !interactions.contains(interaction) &&
          interactomesIds.contains(interaction.getInteractome().getId())
      ));
 
      genes = accumulator.getGenes().collect(toSet());
      accumulator.getInteractions().forEach(interactions::add);
      
      notifyDegreeCalculusFinished(event, accumulator, degree);
    }

    notifyCalculusFinished(event);
  }
  
  private void notifyCalculusStarted(SameSpeciesInteractionsRequestEvent requestEvent) {
    this.startEvents.fire(new SameSpeciesCalculusStartedEvent(requestEvent));
  }
  
  private void notifyDegreeCalculusStarted(SameSpeciesInteractionsRequestEvent requestEvent, final int currentDegree) {
    this.startDegreeEvents.fire(new SameSpeciesCalculusDegreeStartedEvent(requestEvent, currentDegree));
  }

  private void notifyDegreeCalculusFinished(
    SameSpeciesInteractionsRequestEvent requestEvent,
    InteractionGroupsAccumulator accumulator, int currentDegree
  ) {
    final Set<GeneInteraction> interactions = accumulator.getGroupsByDegree(currentDegree)
      .map(group -> new GeneInteraction(
        group.getGeneA().getId(),
        group.getGeneB().getId(),
        group.getInteractomes()
          .mapToInt(Interactome::getId)
        .toArray()
      ))
    .collect(toSet());
    
    this.finishDegreeEvents.fire(new SameSpeciesCalculusDegreeFinishedEvent(requestEvent, currentDegree, interactions));
  }

  private void notifyCalculusFinished(SameSpeciesInteractionsRequestEvent requestEvent) {
    this.finishEvents.fire(new SameSpeciesCalculusFinishedEvent(requestEvent));
  }
  
  private static class InteractionGroupsAccumulator {
    private final Set<InteractionGroup> groups;
    
    public InteractionGroupsAccumulator() {
      this.groups = new HashSet<>();
    }
    
    public void addGroups(Collection<InteractionGroup> groups) {
      groups.stream()
        .filter(group -> !this.hasGroup(group))
        .forEach(this.groups::add);
    }
    
    public Stream<InteractionGroup> getGroupsByDegree(int degree) {
      return groups.stream()
        .filter(group -> group.getDegree() == degree);
    }
    
    public Stream<Gene> getGenes() {
      return this.groups.stream()
        .map(InteractionGroup::getInteractingGenes)
        .flatMap(InteractingGenes::getGenes)
        .distinct();
    }
    
    public Stream<Interaction> getInteractions() {
      return this.groups.stream()
        .flatMap(InteractionGroup::getInteractions)
        .distinct();
    }
    
    private boolean hasGroup(InteractionGroup group) {
      return this.groups.stream()
        .anyMatch(storedGroup -> storedGroup.getInteractingGenes().equals(group.getInteractingGenes()));
    }
  }
  
  private Set<InteractionGroup> calculateInteractions(
    int degree, Collection<Gene> genes, Predicate<Interaction> interactionFilter
  ) {
    return genes.stream()
      .flatMap(Gene::getInteractions)
      .filter(interactionFilter)
      .distinct()
      .collect(groupingBy(InteractingGenes::new, toSet()))
      .values().stream()
      .map(interactions -> new InteractionGroup(interactions, degree))
    .collect(toSet());
  }

}
