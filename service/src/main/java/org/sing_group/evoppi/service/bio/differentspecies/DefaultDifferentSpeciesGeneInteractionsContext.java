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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;

public class DefaultDifferentSpeciesGeneInteractionsContext
implements DifferentSpeciesGeneInteractionsContext, Serializable {
  private static final long serialVersionUID = 1L;
  
  private transient final DifferentSpeciesGeneInteractionsPipeline pipeline;
  private final DifferentSpeciesGeneInteractionsConfiguration configuration;
  private transient final DifferentSpeciesGeneInteractionsEventManager eventManager;
  
  private final Optional<Path> referenceFastaPath;
  private final Optional<Path> targetFastaPath;
  private final Optional<Collection<GeneInteraction>> referenceInteractions;
  private final Optional<Collection<GeneInteraction>> targetInteractions;
  private final Optional<Collection<BlastResult>> blastResults;

  DefaultDifferentSpeciesGeneInteractionsContext(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager,
      Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
    );
  }
  
  DefaultDifferentSpeciesGeneInteractionsContext(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager,
    Optional<Path> referenceFastaPath,
    Optional<Path> targetFastaPath,
    Optional<Stream<GeneInteraction>> referenceInteractions,
    Optional<Stream<GeneInteraction>> targetInteractions,
    Optional<Stream<BlastResult>> blastResults
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);
    
    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    this.referenceInteractions = referenceInteractions.map(result -> result.collect(toSet()));
    this.targetInteractions = targetInteractions.map(result -> result.collect(toSet()));
    this.blastResults = blastResults.map(result -> result.collect(toSet()));
  }

  @Override
  public DifferentSpeciesGeneInteractionsConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public DifferentSpeciesGeneInteractionsEventManager getEventManager() {
    return this.eventManager;
  }

  @Override
  public DifferentSpeciesGeneInteractionsPipeline getPipeline() {
    return this.pipeline;
  }

  @Override
  public Optional<Path> getReferenceFastaPath() {
    return this.referenceFastaPath;
  }

  @Override
  public Optional<Path> getTargetFastaPath() {
    return this.targetFastaPath;
  }

  @Override
  public Optional<Stream<GeneInteraction>> getReferenceInteractions() {
    return this.referenceInteractions.map(Collection::stream);
  }

  @Override
  public Optional<Stream<GeneInteraction>> getTargetInteractions() {
    return this.targetInteractions.map(Collection::stream);
  }

  @Override
  public Optional<Stream<BlastResult>> getBlastResults() {
    return this.blastResults.map(Collection::stream);
  }
}
