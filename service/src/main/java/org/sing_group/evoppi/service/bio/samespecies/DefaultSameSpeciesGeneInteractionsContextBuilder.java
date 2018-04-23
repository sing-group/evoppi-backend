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

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.InteractionIds;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;

public class DefaultSameSpeciesGeneInteractionsContextBuilder implements SameSpeciesGeneInteractionsContextBuilder {
  private final SameSpeciesGeneInteractionsPipeline pipeline;
  private final SameSpeciesGeneInteractionsConfiguration configuration;
  private final SameSpeciesGeneInteractionsEventManager eventManager;

  private final Map<Integer, Set<InteractionIds>> interactions;
  private Set<InteractionIds> completedInteractions;

  DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsContext context
  ) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getInteractionsByDegree(),
      context.hasCompletedInteractions() ? context.getCompletedInteractions().collect(toSet()) : null
    );
  }

  DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, emptyMap(), null);
  }

  DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager,
    Map<Integer, Set<InteractionIds>> interactions,
    Set<InteractionIds> completedInteractions
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.interactions = new HashMap<>(interactions);
    this.completedInteractions = completedInteractions == null ? null : new HashSet<>(completedInteractions);
  }
  
  @Override
  public SameSpeciesGeneInteractionsContextBuilder setInteractions(int degree, Stream<InteractionIds> interactions) {
    this.interactions.put(degree, interactions.collect(toSet()));
    
    return this;
  }
  
  @Override
  public SameSpeciesGeneInteractionsContextBuilder setCompletedInteractions(Stream<InteractionIds> interactions) {
    this.completedInteractions = interactions.collect(toSet());
    
    return this;
  }

  @Override
  public SameSpeciesGeneInteractionsContext build() {
    return new DefaultSameSpeciesGeneInteractionsContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.interactions,
      this.completedInteractions
    );
  }
}
