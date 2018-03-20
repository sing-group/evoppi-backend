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
package org.sing_group.evoppi.service.bio.samespecies.event;

import java.io.Serializable;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class DefaultSameSpeciesGeneInteractionsEvent
implements SameSpeciesGeneInteractionsEvent, WorkStepEvent, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final SameSpeciesGeneInteractionsContext context;
  private final String description;
  private final double progress;
  private final ExecutionStatus status;
  
  public DefaultSameSpeciesGeneInteractionsEvent(
    SameSpeciesGeneInteractionsContext context, String description, double progress, ExecutionStatus status
  ) {
    this.context = context;
    this.description = description;
    this.progress = progress;
    this.status = status;
  }

  @Override
  public SameSpeciesGeneInteractionsContext getContext() {
    return this.context;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public double getProgress() {
    return this.progress;
  }

  @Override
  public ExecutionStatus getStatus() {
    return this.status;
  }

  @Override
  public String getWorkId() {
    return this.context.getConfiguration().getWorkId();
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return this.status;
  }

}
