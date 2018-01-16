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

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusDegreeFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusDegreeStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.SameSpeciesInteractionEventManager;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultSameSpeciesInteractionEventManager implements SameSpeciesInteractionEventManager {
  @Inject
  public InteractionService interactionsService;

  public DefaultSameSpeciesInteractionEventManager() {}
  
  @Override
  public void manageStart(@Observes SameSpeciesCalculusStartedEvent event) {
    final InteractionsResult result = this.interactionsService.getResult(event.getResultId());
    
    result.running();
  }
  
  @Override
  public void manageDegreeCalculusStart(@Observes SameSpeciesCalculusDegreeStartedEvent event) {}
  
  @Override
  public void manageDegreeCalculusFinish(@Observes SameSpeciesCalculusDegreeFinishedEvent event) {
    final InteractionsResult result = this.interactionsService.getResult(event.getResultId());
    
    event.getInteractions().forEach(interaction -> result.addInteraction(
      interaction.getGeneAId(),
      interaction.getGeneBId(),
      event.getCurrentDegree(),
      interaction.getInteractomeIds().toArray()
    ));
  }
  
  @Override
  public void manageFinish(@Observes SameSpeciesCalculusFinishedEvent event) {
    final InteractionsResult result = this.interactionsService.getResult(event.getResultId());
    
    result.completed();
  }
}
