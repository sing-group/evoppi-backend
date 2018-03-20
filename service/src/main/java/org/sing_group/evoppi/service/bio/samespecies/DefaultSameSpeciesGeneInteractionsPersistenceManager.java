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
package org.sing_group.evoppi.service.bio.samespecies;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;

@Stateless
@PermitAll
@Transactional(NOT_SUPPORTED)
public class DefaultSameSpeciesGeneInteractionsPersistenceManager
implements SameSpeciesGeneInteractionsPersistenceManager {
  @Inject
  public InteractionService interactionsService;
  
  @Override
  @Transactional(REQUIRES_NEW)
  public void manageEvent(
    @Observes SameSpeciesGeneInteractionsEvent event
  ) {
    final SameSpeciesGeneInteractionsContext context = event.getContext();
    final String resultId = context.getConfiguration().getResultId();
    
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(resultId);
    
    switch (event.getStatus()) {
    case RUNNING:
      result.setRunning();
      break;
    case COMPLETED:
      final Stream<GeneInteraction> interactions = context.getInteractions()
        .orElseThrow(IllegalStateException::new);
      
      interactions.forEach(interaction -> result.addInteraction(
        interaction.getGeneAId(),
        interaction.getGeneBId(),
        interaction.getInteractomeDegrees()
      ));
      
      result.setFinished();
      
      break;
    case FAILED:
      result.setFailed(event.getDescription());
      
      break;
    default:
    }
  }
}
