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

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
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
  
  public Optional<Path> getReferenceFastaPath();

  public Optional<Path> getTargetFastaPath();

  public Optional<Stream<GeneInteraction>> getReferenceInteractions();
  
  public default Optional<IntStream> getReferenceGeneIds() {
    return extractGeneIds(this.getReferenceInteractions());
  }

  public Optional<Stream<GeneInteraction>> getTargetInteractions();
  
  public default Optional<IntStream> getTargetGeneIds() {
    return extractGeneIds(this.getTargetInteractions());
  }

  public Optional<Stream<BlastResult>> getBlastResults();
  
  static Optional<IntStream> extractGeneIds(Optional<Stream<GeneInteraction>> interactions) {
    return interactions.map(
      interaction -> interaction
        .flatMapToInt(GeneInteraction::getGeneIds)
      .distinct()
    );
  }
}
