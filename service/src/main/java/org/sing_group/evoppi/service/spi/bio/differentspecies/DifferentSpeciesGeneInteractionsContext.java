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
package org.sing_group.evoppi.service.spi.bio.differentspecies;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.entity.InteractionIds;
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
  public Optional<IntStream> getReferenceInteractionsDegrees();
  
  public Optional<Stream<InteractionIds>> getReferenceInteractionsWithDegree(int degree);
  
  public default Optional<Map<Integer, Set<InteractionIds>>> getReferenceInteractionsByDegree() {
    return this.getReferenceInteractionsDegrees().map(referenceInteractionsDegrees ->
      referenceInteractionsDegrees
        .boxed()
      .collect(toMap(
        identity(),
        degree -> this.getReferenceInteractionsWithDegree(degree).get().collect(toSet())
      ))
    );
  }
  
  public default Optional<Boolean> hasReferenceInteractionsWithDegree(int degree) {
    return this.getReferenceInteractionsDegrees().map(
      referenceInteractionsDegrees -> referenceInteractionsDegrees
        .anyMatch(d -> d == degree)
    );
  }
  
  public default Optional<Stream<InteractionIds>> getReferenceInteractions() {
    return this.getReferenceInteractionsByDegree().map(
      referenceInteractionsByDegree -> referenceInteractionsByDegree.values().stream()
        .flatMap(Set::stream)
    );
  }
  
  public default Optional<IntStream> getReferenceGeneIds() {
    return this.getReferenceInteractions().map(
      referenceInteractions -> referenceInteractions
        .flatMapToInt(InteractionIds::getGenes)
        .distinct()
    );
  }
  
  public Optional<Stream<InteractionIds>> getReferenceCompletedInteractions();
  
  public default Optional<IntStream> getReferenceCompletedGeneIds() {
    return this.getReferenceCompletedInteractions().map(
      interactions -> interactions
        .flatMapToInt(InteractionIds::getGenes)
        .distinct()
    );
  }
  
  public Optional<Path> getReferenceFastaPath();
  
  public Optional<Path> getTargetFastaPath();

  public Optional<Stream<BlastResult>> getBlastResults();

  public Optional<IntStream> getTargetInteractionsDegrees();
  
  public Optional<Stream<InteractionIds>> getTargetInteractionsWithDegree(int degree);
  
  public default Optional<Map<Integer, Set<InteractionIds>>> getTargetInteractionsByDegree() {
    return this.getTargetInteractionsDegrees().map(targetInteractionsDegrees ->
      targetInteractionsDegrees
        .boxed()
      .collect(toMap(
        identity(),
        degree -> this.getTargetInteractionsWithDegree(degree).get().collect(toSet())
      ))
    );
  }
  
  public default Optional<Boolean> hasTargetInteractionsWithDegree(int degree) {
    return this.getTargetInteractionsDegrees().map(
      targetInteractionsDegrees -> targetInteractionsDegrees
        .anyMatch(d -> d == degree)
    );
  }
  
  public default Optional<Stream<InteractionIds>> getTargetInteractions() {
    return this.getTargetInteractionsByDegree().map(
      TargetInteractionsByDegree -> TargetInteractionsByDegree.values().stream()
        .flatMap(Set::stream)
    );
  }
  
  public default Optional<IntStream> getTargetGeneIds() {
    return this.getTargetInteractions().map(
      targetInteractions -> targetInteractions
        .flatMapToInt(InteractionIds::getGenes)
        .distinct()
    );
  }
  
  public Optional<Stream<InteractionIds>> getTargetCompletedInteractions();
  
  public default Optional<IntStream> getTargetCompletedGeneIds() {
    return this.getTargetCompletedInteractions().map(
      interactions -> interactions
        .flatMapToInt(InteractionIds::getGenes)
        .distinct()
    );
  }
}
