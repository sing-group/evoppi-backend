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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;

@Stateless
@PermitAll
public class DefaultSameSpeciesGeneInteractionsEventManager
implements SameSpeciesGeneInteractionsEventManager {
  @Inject
  private Event<SameSpeciesGeneInteractionsEvent> events;

  @Override
  public void fireEvent(SameSpeciesGeneInteractionsContext context, String description, double progress, ExecutionStatus status) {
    System.out.println(description);
    this.events.fire(new DefaultSameSpeciesGeneInteractionsEvent(context, description, progress, status));
  }

  @Override
  public  void fireStepEvent(
    SameSpeciesGeneInteractionsStep step, SameSpeciesGeneInteractionsContext context, String description, double progress
  ) {
    final double currentProgress = calculateStepEventProgress(step, context, progress);
    
    this.fireEvent(context, description, currentProgress, ExecutionStatus.RUNNING);
  }

  private static double calculateStepEventProgress(
    SameSpeciesGeneInteractionsStep step, SameSpeciesGeneInteractionsContext context, double progress
  ) {
    final SameSpeciesGeneInteractionsPipeline pipeline = context.getPipeline();
    final double stepIndex = step.getOrder();
    final double stepTotal = pipeline.countTotalSteps();
    
    final double stepProgress = (stepIndex - 1) / stepTotal;
    final double stepSize = 1 / stepTotal;
    
    return stepProgress + progress * stepSize;
  }
}
