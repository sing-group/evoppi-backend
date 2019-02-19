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

package org.sing_group.evoppi.service.bio.differentspecies;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
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
  
  private final Map<Integer, Set<HasGeneInteractionIds>> referenceInteractions;
  private final Set<HasGeneInteractionIds> referenceCompletedInteractions;
  
  private final Path referenceFastaPath;
  private final Path targetFastaPath;
  
  private final Set<BlastResult> blastResults;
  
  private final Map<Integer, Set<HasGeneInteractionIds>> targetInteractions;
  private final Set<HasGeneInteractionIds> targetCompletedInteractions;

  DefaultDifferentSpeciesGeneInteractionsContext(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null, null, null, null, null, null);
  }
  
  DefaultDifferentSpeciesGeneInteractionsContext(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager,
    Map<Integer, Set<HasGeneInteractionIds>> referenceInteractions,
    Collection<HasGeneInteractionIds> referenceCompletedInteractions,
    Path referenceFastaPath,
    Path targetFastaPath,
    Collection<BlastResult> blastResults,
    Map<Integer, Set<HasGeneInteractionIds>> targetInteractions,
    Collection<HasGeneInteractionIds> targetCompletedInteractions
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);

    this.referenceInteractions = referenceInteractions == null ? null : new HashMap<>(referenceInteractions);
    this.referenceCompletedInteractions = referenceCompletedInteractions == null ? null : new HashSet<>(referenceCompletedInteractions);
    
    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    
    this.blastResults = blastResults == null ? null : new HashSet<>(blastResults);

    this.targetInteractions = targetInteractions == null ? null : new HashMap<>(targetInteractions);
    this.targetCompletedInteractions = targetCompletedInteractions == null ? null : new HashSet<>(targetCompletedInteractions);
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
  public Optional<Map<Integer, Set<HasGeneInteractionIds>>> getReferenceInteractionsByDegree() {
    return Optional.ofNullable(this.referenceInteractions).map(Collections::unmodifiableMap);
  }

  @Override
  public Optional<Stream<HasGeneInteractionIds>> getReferenceCompletedInteractions() {
    return Optional.ofNullable(this.referenceCompletedInteractions)
      .map(Set::stream);
  }

  @Override
  public Optional<Path> getReferenceFastaPath() {
    return Optional.ofNullable(this.referenceFastaPath);
  }

  @Override
  public Optional<Path> getTargetFastaPath() {
    return Optional.ofNullable(this.targetFastaPath);
  }

  @Override
  public Optional<Stream<BlastResult>> getBlastResults() {
    return Optional.ofNullable(this.blastResults).map(Set::stream);
  }

  @Override
  public Optional<Map<Integer, Set<HasGeneInteractionIds>>> getTargetInteractionsByDegree() {
    return Optional.ofNullable(this.targetInteractions).map(Collections::unmodifiableMap);
  }

  @Override
  public Optional<Stream<HasGeneInteractionIds>> getTargetCompletedInteractions() {
    return Optional.ofNullable(this.targetCompletedInteractions)
      .map(Set::stream);
  }
}
