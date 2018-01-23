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
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.entity.bio.InteractingGenes;
import org.sing_group.evoppi.service.entity.bio.InteractionGroup;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;

@Default
public class DefaultInteractionsCalculator implements InteractionsCalculator {
  @Transactional(MANDATORY)
  @Override
  public void calculateInteractions(Gene gene, Collection<Interactome> interactomes, int maxDegree) {
    InteractionsCalculator.super.calculateInteractions(gene, interactomes, maxDegree);
  }
  
  @Transactional(MANDATORY)
  @Override
  public void calculateInteractions(Gene gene, Collection<Interactome> interactomes, int maxDegree, InteractionsCalculusCallback callback) {
    callback.calculusStarted();
    
    final InteractionGroupsAccumulator accumulator = new InteractionGroupsAccumulator();

    Set<Gene> genes = singleton(gene);
    Set<Interaction> interactions = new HashSet<>();
    for (int degree = 1; degree <= maxDegree; degree++) {
      callback.degreeCalculusStarted(degree);
      
      accumulator.addGroups(calculateInteractions(
        degree,
        genes,
        interaction ->
          !interactions.contains(interaction) &&
          interactomes.contains(interaction.getInteractome())
      ));
 
      genes = accumulator.getGenes().collect(toSet());
      accumulator.getInteractions().forEach(interactions::add);
      
      callback.degreeCalculusFinished(degree, accumulator.getInteractionsByDegree(degree));
    }

    callback.calculusFinished();
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
    
    public Stream<GeneInteraction> getInteractionsByDegree(int degree) {
      return this.groups.stream()
        .filter(group -> group.getDegree() == degree)
        .map(group -> new GeneInteraction(
          group.getGeneA().getId(),
          group.getGeneB().getId(),
          group.getInteractomes()
            .mapToInt(Interactome::getId)
          .toArray(),
          degree
        ));
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
