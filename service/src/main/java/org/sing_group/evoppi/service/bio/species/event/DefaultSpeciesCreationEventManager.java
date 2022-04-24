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
package org.sing_group.evoppi.service.bio.species.event;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationStep;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEvent;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEventManager;

@Stateless
@PermitAll
public class DefaultSpeciesCreationEventManager
  implements SpeciesCreationEventManager {
  @Inject
  private Event<SpeciesCreationEvent> events;

  @Override
  public void fireEvent(
    SpeciesCreationContext context, ExecutionStatus status, double progress, String description
  ) {
    this.events.fire(new DefaultSpeciesCreationEvent(context, description, progress, status));
  }

  @Override
  public void fireRunningStepEvent(
    SpeciesCreationStep step, SpeciesCreationContext context, String stepId, StepExecutionStatus stepStatus,
    double progress, String description
  ) {
    this.events.fire(new DefaultSpeciesCreationEvent(context, description, progress, stepId, stepStatus));
  }
}
