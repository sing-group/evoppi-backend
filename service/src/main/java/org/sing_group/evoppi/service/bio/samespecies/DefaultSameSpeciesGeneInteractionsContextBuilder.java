/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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

import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;

public class DefaultSameSpeciesGeneInteractionsContextBuilder implements SameSpeciesGeneInteractionsContextBuilder {
  private final SameSpeciesGeneInteractionsPipeline pipeline;
  private final SameSpeciesGeneInteractionsConfiguration configuration;
  private final SameSpeciesGeneInteractionsEventManager eventManager;

  private Map<Integer, Set<HasGeneInteractionIds>> interactions;
  private Set<HasGeneInteractionIds> completedInteractions;

  DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsContext context
  ) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getInteractionsByDegree().map(HashMap::new).orElse(null),
      context.getCompletedInteractions().map(interactions -> interactions.collect(toSet())).orElse(null)
    );
  }

  DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null);
  }

  private DefaultSameSpeciesGeneInteractionsContextBuilder(
    SameSpeciesGeneInteractionsPipeline pipeline,
    SameSpeciesGeneInteractionsConfiguration configuration,
    SameSpeciesGeneInteractionsEventManager eventManager,
    Map<Integer, Set<HasGeneInteractionIds>> interactions,
    Set<HasGeneInteractionIds> completedInteractions
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.interactions = interactions;
    this.completedInteractions = completedInteractions;
  }
  
  @Override
  public SameSpeciesGeneInteractionsContextBuilder addInteractions(int degree, Stream<HasGeneInteractionIds> interactions) {
    if (this.interactions == null)
      this.interactions = new HashMap<>();
    
    this.interactions.merge(degree, interactions.collect(toSet()), (prev, curr) -> {
      prev.addAll(curr);
      return prev;
    });
    
    return this;
  }
  
  @Override
  public SameSpeciesGeneInteractionsContextBuilder addCompletedInteractions(Stream<HasGeneInteractionIds> interactions) {
    if (this.completedInteractions == null)
      this.completedInteractions = new HashSet<>();
    
    interactions.forEach(this.completedInteractions::add);
    
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
