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

import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.Set;
import java.util.function.Function;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteraction;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultSameSpeciesGeneInteractionsPersistenceManager
implements SameSpeciesGeneInteractionsPersistenceManager {
  @Inject
  private InteractionService interactionsService;
  
  @Inject
  public GeneService geneService;
  
  @Inject
  public InteractomeService interactomeService;
  
  @Override
  public void manageEvent(
    @Observes SameSpeciesGeneInteractionsEvent event
  ) {
    final SameSpeciesGeneInteractionsContext context = event.getContext();
    final String resultId = context.getConfiguration().getResultId();
    
    final SameSpeciesInteractionsResult result = this.interactionsService.getSameSpeciesResult(resultId);
    
    switch (event.getStatus()) {
    case RUNNING:
      if (context.getCompletedInteractions().isPresent()) {
        context.getCompletedInteractions().get()
          .map(this.getGeneInteractionMapper())
          .forEach(interaction -> result.addInteraction(interaction, interaction.getInteractome()));
      } else if (context.getInteractions().isPresent()) {
        final Set<Integer> interactomesWithInterations = result.getQueryInteractomeIds()
          .filter(result::hasInteractionsForInteractome)
          .boxed()
        .collect(toSet());
        
        context.getInteractionsDegrees().get()
          .forEach(degree -> 
            context.getInteractionsWithDegree(degree).get()
              .filter(interaction -> !interactomesWithInterations.contains(interaction.getInteractomeId()))
              .map(this.getGeneInteractionMapper())
              .forEach(interaction -> 
                result.addInteraction(interaction, interaction.getInteractome(), degree)
              )
          );
      }
      
      break;
    default:
    }
  }
  
  private Function<HasGeneInteractionIds, HasGeneInteraction> getGeneInteractionMapper() {
    return interactionIds -> HasGeneInteraction.from(interactionIds, geneService::get, interactomeService::getInteractome);
  }
}
