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
package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.REQUIRED;

import java.util.Collection;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.service.bio.entity.InteractingGenesWithDegree;
import org.sing_group.evoppi.service.bio.entity.InteractionIds;
import org.sing_group.evoppi.service.spi.bio.SingleInteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.event.SingleInteractionsCalculusCallback;

@Default
public class DefaultDifferentSpeciesCalculateReferenceInteractionsGeneInteractionsStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  
  private GeneDAO geneDao;
  private SingleInteractionsCalculator interactionsCalculator;
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  private EntityManager entityManager;
  
  private Integer interactomeId;
  
  DefaultDifferentSpeciesCalculateReferenceInteractionsGeneInteractionsStep() {}
  
  public DefaultDifferentSpeciesCalculateReferenceInteractionsGeneInteractionsStep(
    GeneDAO geneDao,
    SingleInteractionsCalculator interactionsCalculator,
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
  public void setInteractionsCalculator(SingleInteractionsCalculator interactionsCalculator) {
    this.interactionsCalculator = requireNonNull(interactionsCalculator);
  }
  
  @Inject
  public void setContextBuilderFactory(DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }
  
  @Override
  public String getName() {
    return "Reference interactome retrieval: " + this.interactomeId;
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
    
    final Gene gene = this.geneDao.getGene(configuration.getGeneId());

    final DifferentSpeciesGeneInteractionsContextBuilder createBuilder = this.contextBuilderFactory.createBuilderFor(context);
    final BridgeInteractionsCalculusCallback callback = new BridgeInteractionsCalculusCallback(this.interactomeId, createBuilder);
    
    this.interactionsCalculator.calculateInteractions(gene, this.interactomeId, configuration.getMaxDegree(), callback);
    
    this.entityManager.clear(); // Avoids unnecessary persistence check
    
    return callback.getContext();
  }
  
  private class BridgeInteractionsCalculusCallback implements SingleInteractionsCalculusCallback {
    private final int interactomeId;
    private final DifferentSpeciesGeneInteractionsContextBuilder contextBuilder;
    
    public BridgeInteractionsCalculusCallback(int interactomeId, DifferentSpeciesGeneInteractionsContextBuilder contextBuilder) {
      this.interactomeId = interactomeId;
      this.contextBuilder = contextBuilder;
    }
    
    public DifferentSpeciesGeneInteractionsContext getContext() {
      return contextBuilder.build();
    }

    @Override
    public void calculusStarted() {}
    
    @Override
    public void interactionsCalculated(int degree, Collection<InteractingGenesWithDegree> interactions) {
      final Stream<InteractionIds> mappedInteractions = interactions.stream()
        .map(interaction -> new InteractionIds(
          this.interactomeId,
          interaction.getGeneA().getId(),
          interaction.getGeneB().getId()
        ));
      
      contextBuilder.setReferenceInteractions(degree, mappedInteractions);
    }
    
    @Override
    public void calculusFinished() {}
  }
}