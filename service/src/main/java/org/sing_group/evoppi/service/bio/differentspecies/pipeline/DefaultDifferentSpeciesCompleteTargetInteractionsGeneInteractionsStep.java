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
package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.bio.BlastResultOrthologsManager;
import org.sing_group.evoppi.service.bio.GenePairIndexer;
import org.sing_group.evoppi.service.bio.GenePairIndexer.GenePairIndex;
import org.sing_group.evoppi.service.spi.bio.OrthologsManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;

@Default
@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  
  private GenePairIndexer interactomeIndexer;
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  private Integer interactomeId;
  
  DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep() {}

  public DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep(
    GenePairIndexer interactomeIndexer,
    DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory
  ) {
    this.setInteractomeIndexer(interactomeIndexer);
    this.setContextBuilderFactory(contextBuilderFactory);
  }

  @Inject
  public void setInteractomeIndexer(GenePairIndexer interactomeIndexer) {
    this.interactomeIndexer = requireNonNull(interactomeIndexer);
  }
  
  @Inject
  public void setContextBuilderFactory(DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }
  
  public void setInteractomeId(int interactomeId) {
    this.interactomeId = interactomeId;
  }
  
  @Override
  public String getStepId() {
    return SINGLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID;
  }
  
  @Override
  public String getName() {
    return "Target interactome completion: " + this.interactomeId;
  }
  
  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getTargetCompletedInteractions().isPresent();
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    if (this.interactomeId == null)
      throw new IllegalStateException("Interactome id should be set before executing the step");
    
    final GenePairIndex interactomeIndex = this.interactomeIndexer.createForInteractome(this.interactomeId);

    final DifferentSpeciesGeneInteractionsContextBuilder contextBuilder = this.contextBuilderFactory.createBuilderFor(context);
    
    final OrthologsManager orthologsManager = new BlastResultOrthologsManager(context.getBlastResults().get());
    
    final Function<HasGeneInteractionIds, Stream<HasGeneInteractionIds>> interactionToTarget = interactionIds ->
      orthologsManager.mapReferencePairOrthologs(
        interactionIds,
        (genePairIds) -> HasGeneInteractionIds.of(this.interactomeId, genePairIds)
      );

    final Function<HasGeneInteractionIds, Stream<HasGeneInteractionIds>> interactionToReference = interactionIds ->
      orthologsManager.mapTargetPairOrthologs(
        interactionIds,
        (genePairIds) -> HasGeneInteractionIds.of(this.interactomeId, genePairIds)
      );
    
    final Function<HasGeneInteractionIds, IntStream> interactionToReferenceIds = interactionIds ->
      interactionToReference.apply(interactionIds).flatMapToInt(HasGeneInteractionIds::getGeneIds);
      
      // TODO: Revisar. Continuar aquí
    final Stream<HasGeneInteractionIds> completedInteractions = context.getReferenceInteractions()
      .orElseThrow(() -> new IllegalStateException("Context should have reference interactions"))
      .filter(interaction -> interaction.getInteractomeId() != this.interactomeId)
      .flatMap(interactionToTarget)
      .filter(interactomeIndex::has)
      // Checking for genes avoids including interactions that are linked with the query gene and,
      // therefore, that have a known degree that, in fact, would be higher than the maxDegree.
      .filter(interaction -> !context.hasTargetInteractionWithAnyGeneOf(this.interactomeId, interactionToReferenceIds.apply(interaction).toArray()))
      .map(interaction -> HasGeneInteractionIds.of(this.interactomeId, interaction))
      .filter(interaction -> !context.hasCompletedTargetInteraction(interaction))
      .flatMap(interactionToReference)
    .distinct();
    
    contextBuilder.addTargetCompletedInteractions(completedInteractions);
    
    return contextBuilder.build();
  }
}
