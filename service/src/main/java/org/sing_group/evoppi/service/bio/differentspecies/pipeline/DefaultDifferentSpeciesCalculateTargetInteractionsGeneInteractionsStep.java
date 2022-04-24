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
import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.SINGLE_CACULATE_TARGET_INTERACTIONS_STEP_ID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;
import org.sing_group.evoppi.service.bio.BlastResultOrthologsManager;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.OrthologsManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;

@Default
public class DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  
  private GeneDAO geneDao;
  private InteractionsCalculator interactionsCalculator;
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  private EntityManager entityManager;
  
  private Integer interactomeId;
  
  DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep() {}
  
  public DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep(
    GeneDAO geneDao,
    InteractionsCalculator interactionsCalculator,
    DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory,
    EntityManager entityManager
  ) {
    this.setGeneDao(geneDao);
    this.setInteractionsCalculator(interactionsCalculator);
    this.setContextBuilderFactory(contextBuilderFactory);
    this.setEntityManager(entityManager);
  }
  
  public void setInteractomeId(int interactomeId) {
    this.interactomeId = interactomeId;
  }
  
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = requireNonNull(entityManager);
  }
  
  @Inject
  public void setGeneDao(GeneDAO geneDao) {
    this.geneDao = requireNonNull(geneDao);
  }
  
  @Inject
  public void setInteractionsCalculator(InteractionsCalculator interactionsCalculator) {
    this.interactionsCalculator = requireNonNull(interactionsCalculator);
  }
  
  @Inject
  public void setContextBuilderFactory(DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }
  
  @Override
  public String getStepId() {
    return SINGLE_CACULATE_TARGET_INTERACTIONS_STEP_ID;
  }
  
  @Override
  public String getName() {
    return "Target interactome retrieval: " + this.interactomeId;
  }
  
  @Override
  public int getOrder() {
    return -1;
  }
  
  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return false;
  }

  @Transactional(REQUIRED)
  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    if (this.interactomeId == null)
      throw new IllegalStateException("Interactome id should be set before executing the step");
    
    final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    
    final DifferentSpeciesGeneInteractionsContextBuilder createBuilder = this.contextBuilderFactory.createBuilderFor(context);
    final OrthologsManager orthologsManager = new BlastResultOrthologsManager(context.getBlastResults().get());
    final BridgeInteractionsCalculusCallback callback = new BridgeInteractionsCalculusCallback(
      this.interactomeId, createBuilder, orthologsManager
    );
    
    orthologsManager.getOrthologsForReferenceGene(configuration.getQueryGeneId())
      .mapToObj(this.geneDao::getGene)
      .forEach(gene -> this.interactionsCalculator.calculateInteractions(gene, this.interactomeId, configuration.getMaxDegree(), callback));
    
    this.entityManager.clear(); // Avoids unnecessary persistence check
    
    return callback.getContext();
  }
  
  private class BridgeInteractionsCalculusCallback implements InteractionsCalculusCallback {
    private final DifferentSpeciesGeneInteractionsContextBuilder contextBuilder;
    
    private final Function<HasGenePair, Stream<HasGeneInteractionIds>> interactionsMapper;
    
    public BridgeInteractionsCalculusCallback(
      int interactomeId,
      DifferentSpeciesGeneInteractionsContextBuilder contextBuilder,
      OrthologsManager orthologsManager
    ) {
      this.contextBuilder = contextBuilder;
      
      this.interactionsMapper = interaction ->
        orthologsManager.mapTargetPairOrthologs(
          interaction,
          (genePairIds) -> HasGeneInteractionIds.of(interactomeId, genePairIds)
        );
    }
    
    public DifferentSpeciesGeneInteractionsContext getContext() {
      this.contextBuilder.setTargetInteractionsCalculated();
      
      return contextBuilder.build();
    }
    
    @Override
    public void interactionsCalculated(int degree, Collection<HasGenePair> interactions) {
      final Stream<HasGeneInteractionIds> mappedInteractions = interactions.stream()
        .flatMap(this.interactionsMapper);
      
      contextBuilder.addTargetInteractions(degree, mappedInteractions);
    }
  }
}
