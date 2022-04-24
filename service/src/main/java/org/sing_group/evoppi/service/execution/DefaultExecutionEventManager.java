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
package org.sing_group.evoppi.service.execution;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.service.spi.execution.ExecutionEventManager;
import org.sing_group.evoppi.service.spi.execution.WorkService;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultExecutionEventManager implements ExecutionEventManager {
  @Inject
  public WorkService workService;

  @Override
  public void manageWorkStep(@Observes WorkStepEvent event) {
    final WorkEntity work = this.workService.get(event.getWorkId());

    work.addStep(event.getDescription(), event.getProgress());

    switch (event.getWorkStatus()) {
      case SCHEDULED:
        work.setScheduled();
        break;
      case RUNNING:
        work.setRunning();
        break;
      case COMPLETED:
        work.setFinished();
        break;
      case FAILED:
        work.setFailed(event.getDescription());
        break;
      default:
    }
  }
}
