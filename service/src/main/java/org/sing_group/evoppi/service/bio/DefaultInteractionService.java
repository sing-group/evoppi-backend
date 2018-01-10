/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.service.entity.bio.InteractingGenes;
import org.sing_group.evoppi.service.entity.bio.InteractionGroup;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Stateless
@PermitAll
public class DefaultInteractionService implements InteractionService {
  @Inject
  private GeneDAO geneDao;

  @Override
  public Stream<InteractionGroup> findInteractionsByGene(int geneId, int[] interactomes, int maxDegree) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    final Gene gene = this.geneDao.getGene(geneId);
    final Set<Integer> interactomesIds = IntStream.of(interactomes)
      .boxed()
    .collect(toSet());
    
    final InteractionGroupsAccumulator accumulator = new InteractionGroupsAccumulator();
    
    Set<Gene> genes = singleton(gene);
    Set<Interaction> interactions = new HashSet<>();
    for (int degree = 1; degree <= maxDegree; degree++) {
      accumulator.addGroups(calculateInteractions(
        degree,
        genes,
        interaction ->
          !interactions.contains(interaction) &&
          interactomesIds.contains(interaction.getInteractome().getId())
      ));

      genes = accumulator.getGenes().collect(toSet());
      accumulator.getInteractions().forEach(interactions::add);
    }
    
    return accumulator.getGroups();
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
    
    public Stream<InteractionGroup> getGroups() {
      return groups.stream();
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
