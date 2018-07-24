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

package org.sing_group.evoppi.service.bio.differentspecies;

import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.BLAST_ALIGNMENT_STEP_ID;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_CACULATE_REFERENCE_INTERACTIONS_STEP_ID;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_CACULATE_TARGET_INTERACTIONS_STEP_ID;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_COMPLETE_REFERENCE_INTERACTIONS_STEP_ID;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteraction;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;

@Stateless
@PermitAll
@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesGeneInteractionsPersistenceManager
implements DifferentSpeciesGeneInteractionsPersistenceManager {
  @Inject
  public InteractionService interactionsService;
  
  @Inject
  public GeneService geneService;
  
  @Inject
  public InteractomeService interactomeService;

  @Override
  @Transactional(REQUIRES_NEW)
  public void manageEvent(@Observes DifferentSpeciesGeneInteractionsEvent event) {
    final DifferentSpeciesGeneInteractionsContext context = event.getContext();
    final String resultId = context.getConfiguration().getWorkId();
    
    final DifferentSpeciesInteractionsResult result = this.interactionsService.getDifferentSpeciesResult(resultId);

    final Optional<String> step = event.getRunningStepId();
    
    if (step.isPresent() && event.getRunningStepStatus().get() == StepExecutionStatus.FINISHED) {
      switch (step.get()) {
        case SINGLE_CACULATE_REFERENCE_INTERACTIONS_STEP_ID: {
          final Set<Integer> interactomesWithInterations = result.getReferenceInteractomeIds()
            .filter(result::hasInteractionsForInteractome)
            .boxed()
          .collect(toSet());
          
          persistInteractions(
            result,
            interactomesWithInterations,
            context.getReferenceInteractionsDegrees().get(),
            degree -> context.getReferenceInteractionsWithDegree(degree).get()
              .map(this.getGeneInteractionMapper())
          );
          break;
        }
        case SINGLE_COMPLETE_REFERENCE_INTERACTIONS_STEP_ID: {
          final Stream<HasGeneInteraction> interactions = context.getReferenceCompletedInteractions().get()
            .map(this.getGeneInteractionMapper());
          
          persistCompletedInteractions(result, interactions);
          break;
        }
        case BLAST_ALIGNMENT_STEP_ID: {
            this.persistBlastResults(event);
            break;
        }
        case SINGLE_CACULATE_TARGET_INTERACTIONS_STEP_ID: {
          final Set<Integer> interactomesWithInterations = result.getTargetInteractomeIds()
            .filter(result::hasInteractionsForInteractome)
            .boxed()
          .collect(toSet());
          
          persistInteractions(
            result,
            interactomesWithInterations,
            context.getTargetInteractionsDegrees().get(),
            degree -> context.getTargetInteractionsWithDegree(degree).get()
              .map(this.getGeneInteractionMapper())
          );
          
          break;
        }
        case SINGLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID: {
          final Stream<HasGeneInteraction> interactions = context.getTargetCompletedInteractions().get()
            .map(this.getGeneInteractionMapper());
          
          persistCompletedInteractions(result, interactions);
          break;
        }
        default:
      }
    }
  }
  
  private Function<HasGeneInteractionIds, HasGeneInteraction> getGeneInteractionMapper() {
    return interactionIds -> HasGeneInteraction.from(interactionIds, geneService::get, interactomeService::getInteractome);
  }

  private void persistInteractions(
    final DifferentSpeciesInteractionsResult result,
    final Set<Integer> interactomesWithInterations,
    final IntStream interactionDegrees,
    final IntFunction<Stream<HasGeneInteraction>> getInteractionsWithDegree
  ) {
    interactionDegrees.forEach(degree -> 
      getInteractionsWithDegree.apply(degree)
        .filter(interaction -> !interactomesWithInterations.contains(interaction.getInteractomeId()))
        .forEach(interaction -> 
          result.addInteraction(
            interaction,
            interaction.getInteractome(),
            degree
          )
        )
    );
  }

  private void persistCompletedInteractions(
    final DifferentSpeciesInteractionsResult result, final Stream<HasGeneInteraction> interactions
  ) {
    interactions.forEach(interaction -> result.addInteraction(
      interaction,
      interaction.getInteractome()
    ));
  }
  
  private void persistBlastResults(DifferentSpeciesGeneInteractionsEvent event) {
    final String resultId = event.getContext().getConfiguration().getWorkId();
    
    final DifferentSpeciesInteractionsResult result = this.interactionsService.getDifferentSpeciesResult(resultId);
    
    if (!result.hasBlastResults()) {
      final DifferentSpeciesGeneInteractionsContext context = event.getContext();
      
      final Stream<BlastResult> blastResults = context.getBlastResults().orElseThrow(IllegalStateException::new);
      
      blastResults.forEach(result::addBlastResult);
    }
  }
}
