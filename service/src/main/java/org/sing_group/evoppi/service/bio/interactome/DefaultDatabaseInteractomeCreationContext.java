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

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.evoppi.domain.interactome.GeneInteractions;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEventManager;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessingStatistics;

public class DefaultDatabaseInteractomeCreationContext implements DatabaseInteractomeCreationContext, Serializable {
  private static final long serialVersionUID = 1L;

  private transient final DatabaseInteractomeCreationPipeline pipeline;
  private final DatabaseInteractomeCreationConfiguration configuration;
  private transient final DatabaseInteractomeCreationEventManager eventManager;

  private final EvoPpiInteractomeProcessingStatistics statistics;
  private final GeneInteractions interactions;

  DefaultDatabaseInteractomeCreationContext(
    DatabaseInteractomeCreationPipeline pipeline,
    DatabaseInteractomeCreationConfiguration configuration,
    DatabaseInteractomeCreationEventManager eventManager,
    EvoPpiInteractomeProcessingStatistics statistics,
    GeneInteractions interactions
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);
    this.statistics = statistics;
    this.interactions = interactions;
  }

  @Override
  public DatabaseInteractomeCreationConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public DatabaseInteractomeCreationEventManager getEventManager() {
    return this.eventManager;
  }

  @Override
  public DatabaseInteractomeCreationPipeline getPipeline() {
    return this.pipeline;
  }

  @Override
  public Optional<EvoPpiInteractomeProcessingStatistics> getStatistics() {
    return Optional.ofNullable(this.statistics);
  }

  @Override
  public Optional<GeneInteractions> getInteractions() {
    return Optional.ofNullable(this.interactions);
  }
}
