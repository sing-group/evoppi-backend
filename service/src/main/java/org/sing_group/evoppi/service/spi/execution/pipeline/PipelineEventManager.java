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
package org.sing_group.evoppi.service.spi.execution.pipeline;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;

public interface PipelineEventManager<
  C extends PipelineConfiguration,
  PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
  PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
  P extends Pipeline<C, PC, PS, P, PE, PEM>,
  PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
  PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
> {
  public void fireEvent(PC context, ExecutionStatus status, double progress, String description);
//  public void fireRunningEvent(PC context, ExecutionStepStatus status, String stepId, double progress, String description);
  public void fireRunningStepEvent(PS step, PC context, String stepId, StepExecutionStatus stepStatus, double progress, String description);
}
