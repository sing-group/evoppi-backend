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

package org.sing_group.evoppi.service.bio.interactome;

import org.sing_group.evoppi.domain.interactome.GeneInteractions;
import org.sing_group.evoppi.service.spi.bio.interactome.InteractomeCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.interactome.InteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.InteractomeCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.InteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.InteractomeCreationEventManager;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessingStatistics;

public class DefaultInteractomeCreationContextBuilder implements InteractomeCreationContextBuilder {
  private final InteractomeCreationPipeline pipeline;
  private final InteractomeCreationConfiguration configuration;
  private final InteractomeCreationEventManager eventManager;

  private EvoPpiInteractomeProcessingStatistics statistics;
  private GeneInteractions interactions;

  DefaultInteractomeCreationContextBuilder(
    InteractomeCreationContext context
  ) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getStatistics().orElse(null),
      context.getInteractions().orElse(null)
    );
  }

  DefaultInteractomeCreationContextBuilder(
    InteractomeCreationPipeline pipeline,
    InteractomeCreationConfiguration configuration,
    InteractomeCreationEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null);
  }

  private DefaultInteractomeCreationContextBuilder(
    InteractomeCreationPipeline pipeline,
    InteractomeCreationConfiguration configuration,
    InteractomeCreationEventManager eventManager,
    EvoPpiInteractomeProcessingStatistics statistics,
    GeneInteractions interactions
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.statistics = statistics;
    this.interactions = interactions;
  }

  @Override
  public InteractomeCreationContextBuilder addStatistics(EvoPpiInteractomeProcessingStatistics statistics) {
    this.statistics = statistics;
    return this;
  }

  @Override
  public InteractomeCreationContextBuilder addInteractions(GeneInteractions interactions) {
    this.interactions = interactions;
    return this;
  }

  @Override
  public InteractomeCreationContext build() {
    return new DefaultInteractomeCreationContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.statistics,
      this.interactions
    );
  }
}
