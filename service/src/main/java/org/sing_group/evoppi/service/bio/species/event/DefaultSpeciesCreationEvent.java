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

package org.sing_group.evoppi.service.bio.species.event;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEvent;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class DefaultSpeciesCreationEvent
  implements SpeciesCreationEvent, WorkStepEvent, Serializable {
  private static final long serialVersionUID = 1L;

  private final SpeciesCreationContext context;
  private final String description;
  private final double progress;
  private final ExecutionStatus status;
  private final String runningStepId;
  private final StepExecutionStatus runningStepStatus;

  public DefaultSpeciesCreationEvent(
    SpeciesCreationContext context, String description, double progress, ExecutionStatus status
  ) {
    if (status == ExecutionStatus.RUNNING)
      throw new IllegalArgumentException("Running events must declare a stage identifier");

    this.context = context;
    this.description = description;
    this.progress = progress;
    this.status = status;
    this.runningStepId = null;
    this.runningStepStatus = null;
  }

  public DefaultSpeciesCreationEvent(
    SpeciesCreationContext context, String description, double progress, String runningStepId,
    StepExecutionStatus runningStepStatus
  ) {
    this.context = context;
    this.description = description;
    this.progress = progress;
    this.status = ExecutionStatus.RUNNING;
    this.runningStepId = requireNonEmpty(runningStepId);
    this.runningStepStatus = requireNonNull(runningStepStatus);
  }

  @Override
  public SpeciesCreationContext getContext() {
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
  public Optional<String> getRunningStepId() {
    return Optional.ofNullable(this.runningStepId);
  }

  @Override
  public Optional<StepExecutionStatus> getRunningStepStatus() {
    return Optional.ofNullable(this.runningStepStatus);
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
