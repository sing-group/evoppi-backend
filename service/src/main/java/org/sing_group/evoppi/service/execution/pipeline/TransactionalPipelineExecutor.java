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
package org.sing_group.evoppi.service.execution.pipeline;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.execution.pipeline.Pipeline;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineConfiguration;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineContext;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineEvent;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineExecutor;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineStep;

@Stateless
@PermitAll
public class TransactionalPipelineExecutor implements PipelineExecutor {
  @PermitAll
  @Override
  public <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(
    P pipeline, PC context
  ) {
    final PipelineEventManager<C, PC, PS, P, PE, PEM> eventManager = context.getEventManager();
    
    try {
      final String pipelineName = formatStepName(pipeline.getName());
      eventManager.fireEvent(context, "Starting " + pipelineName + " analysis", 0d, ExecutionStatus.RUNNING);
      
      final double stepProgress = 1d / (double) pipeline.countTotalSteps();
      double progress = 0d;
      for (PS step : pipeline.getSteps()) {
        final String name = formatStepName(step.getName());
        
        eventManager.fireEvent(context, "Starting " + name, progress, ExecutionStatus.RUNNING);
        
        context = step.execute(context);
        
        progress += stepProgress;
        eventManager.fireEvent(context, "Completed " + name, progress, ExecutionStatus.RUNNING);
      }
      
      eventManager.fireEvent(context, "Completed " + pipelineName + " analysis", 1d, ExecutionStatus.COMPLETED);
    } catch (RuntimeException re) {
      eventManager.fireEvent(context, re.getMessage(), Double.NaN, ExecutionStatus.FAILED);
    }
  }

  private String formatStepName(String name) {
    return Character.toLowerCase(name.charAt(0)) + name.substring(1);
  }

  @PermitAll
  @Override
  public <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(
    P pipeline, C configuration
  ) {
    PipelineExecutor.super.execute(pipeline, configuration);
  }
}
