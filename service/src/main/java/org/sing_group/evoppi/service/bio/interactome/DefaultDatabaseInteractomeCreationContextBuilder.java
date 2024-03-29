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
package org.sing_group.evoppi.service.bio.interactome;

import org.sing_group.evoppi.domain.interactome.GeneInteractions;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEventManager;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessingStatistics;

public class DefaultDatabaseInteractomeCreationContextBuilder implements DatabaseInteractomeCreationContextBuilder {
  private final DatabaseInteractomeCreationPipeline pipeline;
  private final DatabaseInteractomeCreationConfiguration configuration;
  private final DatabaseInteractomeCreationEventManager eventManager;

  private EvoPpiInteractomeProcessingStatistics statistics;
  private GeneInteractions interactions;

  DefaultDatabaseInteractomeCreationContextBuilder(
    DatabaseInteractomeCreationContext context
  ) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getStatistics().orElse(null),
      context.getInteractions().orElse(null)
    );
  }

  DefaultDatabaseInteractomeCreationContextBuilder(
    DatabaseInteractomeCreationPipeline pipeline,
    DatabaseInteractomeCreationConfiguration configuration,
    DatabaseInteractomeCreationEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null);
  }

  private DefaultDatabaseInteractomeCreationContextBuilder(
    DatabaseInteractomeCreationPipeline pipeline,
    DatabaseInteractomeCreationConfiguration configuration,
    DatabaseInteractomeCreationEventManager eventManager,
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
  public DatabaseInteractomeCreationContextBuilder addStatistics(EvoPpiInteractomeProcessingStatistics statistics) {
    this.statistics = statistics;
    return this;
  }

  @Override
  public DatabaseInteractomeCreationContextBuilder addInteractions(GeneInteractions interactions) {
    this.interactions = interactions;
    return this;
  }

  @Override
  public DatabaseInteractomeCreationContext build() {
    return new DefaultDatabaseInteractomeCreationContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.statistics,
      this.interactions
    );
  }
}
