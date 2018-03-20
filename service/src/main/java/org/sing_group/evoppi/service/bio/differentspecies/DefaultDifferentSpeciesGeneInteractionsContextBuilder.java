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
package org.sing_group.evoppi.service.bio.differentspecies;

import static java.util.Optional.empty;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;

public class DefaultDifferentSpeciesGeneInteractionsContextBuilder implements DifferentSpeciesGeneInteractionsContextBuilder {
  private final DifferentSpeciesGeneInteractionsPipeline pipeline;
  private final DifferentSpeciesGeneInteractionsConfiguration configuration;
  private final DifferentSpeciesGeneInteractionsEventManager eventManager;
  
  private Optional<Path> referenceFastaPath;
  private Optional<Path> targetFastaPath;
  private Optional<Stream<GeneInteraction>> referenceInteractions;
  private Optional<Stream<GeneInteraction>> targetInteractions;
  private Optional<Stream<BlastResult>> blastResults;

  DefaultDifferentSpeciesGeneInteractionsContextBuilder(DifferentSpeciesGeneInteractionsContext context) {
    this(context.getPipeline(), context.getConfiguration(), context.getEventManager(),
      context.getReferenceFastaPath(), context.getTargetFastaPath(),
      context.getReferenceInteractions(), context.getTargetInteractions(),
      context.getBlastResults()
    );
  }
  
  DefaultDifferentSpeciesGeneInteractionsContextBuilder(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, empty(), empty(), empty(), empty(), empty());
  }
  
  DefaultDifferentSpeciesGeneInteractionsContextBuilder(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager,
    Optional<Path> referenceFastaPath,
    Optional<Path> targetFastaPath,
    Optional<Stream<GeneInteraction>> referenceInteractions,
    Optional<Stream<GeneInteraction>> targetInteractions,
    Optional<Stream<BlastResult>> blastResults
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    this.referenceInteractions = referenceInteractions;
    this.targetInteractions = targetInteractions;
    this.blastResults = blastResults;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceFastaPath(Path referenceFastaPath) {
    this.referenceFastaPath = Optional.of(referenceFastaPath);
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetFastaPath(Path targetFastaPath) {
    this.targetFastaPath = Optional.of(targetFastaPath);
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceInteractions(
    Stream<GeneInteraction> referenceInteractions
  ) {
    this.referenceInteractions = Optional.of(referenceInteractions);
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractions(Stream<GeneInteraction> targetInteractions) {
    this.targetInteractions = Optional.of(targetInteractions);
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Stream<BlastResult> blastResult) {
    this.blastResults = Optional.of(blastResult);
    return this;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContext build() {
    return new DefaultDifferentSpeciesGeneInteractionsContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.referenceFastaPath,
      this.targetFastaPath,
      this.referenceInteractions,
      this.targetInteractions,
      this.blastResults
    );
  }
}
