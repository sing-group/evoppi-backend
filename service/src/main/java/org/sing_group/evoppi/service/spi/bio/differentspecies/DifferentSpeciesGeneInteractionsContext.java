/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

package org.sing_group.evoppi.service.spi.bio.differentspecies;

import static java.util.stream.Stream.empty;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineContext;

public interface DifferentSpeciesGeneInteractionsContext
extends PipelineContext<
  DifferentSpeciesGeneInteractionsConfiguration,
  DifferentSpeciesGeneInteractionsContext,
  DifferentSpeciesGeneInteractionsStep,
  DifferentSpeciesGeneInteractionsPipeline,
  DifferentSpeciesGeneInteractionsEvent,
  DifferentSpeciesGeneInteractionsEventManager
> {
  public Optional<Map<Integer, Set<HasGeneInteractionIds>>> getReferenceInteractionsByDegree();
  
  public default Optional<IntStream> getReferenceInteractionsDegrees() {
    return this.getReferenceInteractionsByDegree().map(
      interactions -> interactions.keySet().stream()
        .mapToInt(Integer::intValue)
    );
  }
  
  public default Optional<Stream<HasGeneInteractionIds>> getReferenceInteractionsWithDegree(int degree) {
    return this.getReferenceInteractionsByDegree()
      .map(interactions -> interactions.get(degree))
      .map(Set::stream);
  }
  
  public default Optional<Stream<HasGeneInteractionIds>> getReferenceInteractionsForInteractome(int interactomeId) {
    return this.getReferenceInteractions().map(
      interactions -> interactions.filter(interaction -> interaction.getInteractomeId() == interactomeId)
    );
  }
  
  public default Optional<Boolean> hasReferenceInteractionsWithDegree(int degree) {
    return this.getReferenceInteractionsDegrees().map(
      interactions -> interactions.anyMatch(interactionDegree -> interactionDegree == degree)
    );
  }
  
  public default Optional<Stream<HasGeneInteractionIds>> getReferenceInteractions() {
    return this.getReferenceInteractionsByDegree().map(
      interactions -> interactions.values().stream()
        .flatMap(Set::stream)
    );
  }
  
  public default Optional<IntStream> getReferenceGeneIds() {
    return this.getReferenceInteractions().map(
      interactions -> interactions
        .flatMapToInt(HasGeneInteractionIds::getGeneIds)
        .distinct()
    );
  }
  
  public Optional<Stream<HasGeneInteractionIds>> getReferenceCompletedInteractions();
  
  public default Optional<IntStream> getReferenceCompletedGeneIds() {
    return this.getReferenceCompletedInteractions().map(
      interactions -> interactions
        .flatMapToInt(HasGeneInteractionIds::getGeneIds)
        .distinct()
    );
  }
  
  public default boolean hasReferenceInteractionWithAnyGeneOf(int interactomeId, int ... geneId) {
    final Predicate<HasGeneInteractionIds> interactionHasAnyGene = interaction ->
      IntStream.of(geneId).anyMatch(id -> interaction.getGeneAId() == id || interaction.getGeneBId() == id);
    
    return this.getReferenceInteractionsForInteractome(interactomeId).orElse(Stream.empty())
      .anyMatch(interactionHasAnyGene);
  }
  
  public Optional<Path> getReferenceFastaPath();
  
  public Optional<Path> getTargetFastaPath();

  public Optional<Stream<BlastResult>> getBlastResults();
  
  public Optional<Map<Integer, Set<HasGeneInteractionIds>>> getTargetInteractionsByDegree();

  public default Optional<IntStream> getTargetInteractionsDegrees() {
    return this.getTargetInteractionsByDegree().map(
      interactions -> interactions.keySet().stream()
        .mapToInt(Integer::intValue)
    );
  }
  
  public default Optional<Stream<HasGeneInteractionIds>> getTargetInteractionsWithDegree(int degree) {
    return this.getTargetInteractionsByDegree()
      .map(interactions -> interactions.get(degree))
      .map(Set::stream);
  }
  
  public default Optional<Boolean> hasTargetInteractionsWithDegree(int degree) {
    return this.getTargetInteractionsDegrees().map(
      interactions -> interactions.anyMatch(interactionDegree -> interactionDegree == degree)
    );
  }
  
  public default Optional<Stream<HasGeneInteractionIds>> getTargetInteractions() {
    return this.getTargetInteractionsByDegree().map(
      interactions -> interactions.values().stream()
        .flatMap(Set::stream)
    );
  }
  
  public default Optional<IntStream> getTargetGeneIds() {
    return this.getTargetInteractions().map(
      interactions -> interactions
        .flatMapToInt(HasGeneInteractionIds::getGeneIds)
        .distinct()
    );
  }
  
  public Optional<Stream<HasGeneInteractionIds>> getTargetCompletedInteractions();
  
  public default Optional<IntStream> getTargetCompletedGeneIds() {
    return this.getTargetCompletedInteractions().map(
      interactions -> interactions
        .flatMapToInt(HasGeneInteractionIds::getGeneIds)
        .distinct()
    );
  }

  public default boolean hasCompletedTargetInteraction(HasGeneInteractionIds interaction) {
    return this.getReferenceCompletedInteractions().orElse(empty())
      .anyMatch(targetInteraction -> targetInteraction.equals(interaction));
  }

  public default boolean hasTargetInteractionWithAnyGeneOf(int interactomeId, int ... geneId) {
    final Predicate<HasGeneInteractionIds> interactionHasAnyGene = interaction ->
      IntStream.of(geneId).anyMatch(id -> interaction.getGeneAId() == id || interaction.getGeneBId() == id);
    
    return this.getTargetInteractionsForInteractome(interactomeId).orElse(Stream.empty())
      .anyMatch(interactionHasAnyGene);
  }

  public default Optional<Stream<HasGeneInteractionIds>> getTargetInteractionsForInteractome(int interactomeId) {
    return this.getTargetInteractions().map(
      interactions -> interactions.filter(interaction -> interaction.getInteractomeId() == interactomeId)
    );
  }
}
