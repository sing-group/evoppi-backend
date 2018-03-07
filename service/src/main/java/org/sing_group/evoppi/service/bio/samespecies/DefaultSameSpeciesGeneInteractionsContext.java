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
package org.sing_group.evoppi.service.bio.samespecies;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;

public class DefaultSameSpeciesGeneInteractionsContext
implements SameSpeciesGeneInteractionsContext, Serializable {
  private static final long serialVersionUID = 1L;
  
  private transient final SameSpeciesGeneInteractionsPipeline pipeline;
  
  private final SameSpeciesGeneInteractionsConfiguration configuration;
  
  private transient final SameSpeciesGeneInteractionsEventManager eventManager;
  
  private final Optional<Set<GeneInteraction>> interactions;

  public DefaultSameSpeciesGeneInteractionsContext(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, Optional.empty());
  }

  DefaultSameSpeciesGeneInteractionsContext(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager,
    Stream<GeneInteraction> interactions
  ) {
    this(pipeline, configuration, eventManager, Optional.of(interactions.collect(toSet())));
  }
  
  DefaultSameSpeciesGeneInteractionsContext(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager,
    Optional<Set<GeneInteraction>> interactions
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);
    this.interactions = requireNonNull(interactions);
  }

  @Override
  public SameSpeciesGeneInteractionsEventManager getEventManager() {
    return this.eventManager;
  }
  
  @Override
  public SameSpeciesGeneInteractionsConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public SameSpeciesGeneInteractionsPipeline getPipeline() {
    return this.pipeline;
  }
  
  @Override
  public Optional<Stream<GeneInteraction>> getInteractions() {
    return interactions.map(Set::stream);
  }
  
  @Override
  public SameSpeciesGeneInteractionsContext setInteractions(Stream<GeneInteraction> interactions) {
    return new DefaultSameSpeciesGeneInteractionsContext(this.pipeline, this.configuration, this.eventManager, interactions);
  }
  
  @Override
  public SameSpeciesGeneInteractionsContext setInteractions(Collection<GeneInteraction> interactions) {
    return this.setInteractions(interactions.stream());
  }
}
