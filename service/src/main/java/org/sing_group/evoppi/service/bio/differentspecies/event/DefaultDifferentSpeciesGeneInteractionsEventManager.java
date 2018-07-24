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

package org.sing_group.evoppi.service.bio.differentspecies.event;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;

public class DefaultDifferentSpeciesGeneInteractionsEventManager
implements DifferentSpeciesGeneInteractionsEventManager {
  @Inject
  private Event<DifferentSpeciesGeneInteractionsEvent> events;

  @Override
  public void fireEvent(DifferentSpeciesGeneInteractionsContext context, ExecutionStatus status, double progress, String description) {
    this.events.fire(new DefaultDifferentSpeciesGeneInteractionsEvent(context, description, progress, status));
  }
//  
//  @Override
//  public void fireRunningEvent(
//    DifferentSpeciesGeneInteractionsContext context, String stageId, double progress, String description
//  ) {
//    this.events.fire(new DefaultDifferentSpeciesGeneInteractionsEvent(context, description, progress, stageId));
//  }

  @Override
  public  void fireRunningStepEvent(
    DifferentSpeciesGeneInteractionsStep step, DifferentSpeciesGeneInteractionsContext context, String stepId, StepExecutionStatus stepStatus, double progress, String description
  ) {
    final double currentProgress = calculateStepEventProgress(step, context, progress);
    
    this.events.fire(new DefaultDifferentSpeciesGeneInteractionsEvent(context, description, currentProgress, stepId, stepStatus));
  }

  private static double calculateStepEventProgress(
    DifferentSpeciesGeneInteractionsStep step, DifferentSpeciesGeneInteractionsContext context, double progress
  ) {
    final DifferentSpeciesGeneInteractionsPipeline pipeline = context.getPipeline();
    final double stepIndex = step.getOrder();
    final double stepTotal = pipeline.countTotalSteps();
    
    final double stepProgress = (stepIndex - 1) / stepTotal;
    final double stepSize = 1 / stepTotal;
    
    return stepProgress + progress * stepSize;
  }
}
