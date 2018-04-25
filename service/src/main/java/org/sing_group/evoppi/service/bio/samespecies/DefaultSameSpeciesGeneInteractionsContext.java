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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.InteractionIds;
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
  
  private final Map<Integer, Set<InteractionIds>> interactions;
  private final Set<InteractionIds> completedInteractions;

  DefaultSameSpeciesGeneInteractionsContext(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager,
    Map<Integer, Set<InteractionIds>> interactions,
    Collection<InteractionIds> completedInteractions
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);
    this.interactions = interactions == null ? null : new HashMap<>(interactions);
    this.completedInteractions = completedInteractions == null ? null : new HashSet<>(completedInteractions);
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
  public Optional<Map<Integer, Set<InteractionIds>>> getInteractionsByDegree() {
    return Optional.ofNullable(this.interactions).map(Collections::unmodifiableMap);
  }

  @Override
  public Optional<Stream<InteractionIds>> getCompletedInteractions() {
    return Optional.ofNullable(this.completedInteractions)
      .map(Set::stream);
  }
}
