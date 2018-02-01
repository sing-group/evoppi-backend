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
package org.sing_group.evoppi.service.bio;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.SameSpeciesInteractionEventManager;

@Stateless
@PermitAll
@Transactional(NOT_SUPPORTED)
public class DefaultSameSpeciesInteractionPersistenceManager implements SameSpeciesInteractionEventManager {
  @Inject
  public InteractionService interactionsService;

  public DefaultSameSpeciesInteractionPersistenceManager() {}
  
  @Transactional(REQUIRES_NEW)
  @Override
  public void manageStart(@Observes SameSpeciesCalculusStartedEvent event) {
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(event.getResultId());
    
    result.setRunning();
  }
  
  @Override
  public void manageInteractionCalculusStart(SameSpeciesInteractionsCalculusStartedEvent event) {}
  
  @Transactional(REQUIRES_NEW)
  @Override
  public void manageInteractionCalculusFinish(@Observes SameSpeciesInteractionCalculusFinishedEvent event) {
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(event.getResultId());
    
    event.getInteractions().forEach(interaction -> result.addInteraction(
      interaction.getGeneAId(),
      interaction.getGeneBId(),
      interaction.getDegree(),
      interaction.getInteractomeIds().toArray()
    ));
  }
  
  @Transactional(REQUIRES_NEW)
  @Override
  public void manageFinish(@Observes SameSpeciesCalculusFinishedEvent event) {
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(event.getResultId());
    
    result.setFinished();
  }

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageFailure(@Observes SameSpeciesCalculusFailedEvent event) {
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(event.getResultId());
    
    result.setFailed(event.getCause());
  }
}
