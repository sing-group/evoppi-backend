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

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback.SimpleInteractionsCalculusCallback;

@Transactional(REQUIRES_NEW)
public class DefaultDifferentSpeciesGeneInteractionsCalculateReferenceInteractionsStep
implements DifferentSpeciesGeneInteractionsStep {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private InteractionsCalculator interactionsCalculator;
  
  @Inject
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;

  @Override
  public String getName() {
    return "Reference genome interactions calculus";
  }
  
  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getReferenceInteractions().isPresent();
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    
    final Interactome referenceInteractome = this.interactomeDao.getInteractome(configuration.getReferenceInteractome());
    final Gene referenceGene = this.geneDao.getGene(configuration.getGeneId());
    
    final Set<GeneInteraction> referenceInteractions = this.getGeneInteractions(referenceGene, referenceInteractome, configuration.getMaxDegree())
      .collect(toSet());
    
    return this.contextBuilderFactory.createBuilderFor(context)
      .setReferenceInteractions(referenceInteractions)
    .build();
  }
  
  private Stream<GeneInteraction> getGeneInteractions(Gene gene, Interactome interactome, int maxDegree) {
    final InteractionsCallback callback = new InteractionsCallback();
    
    this.interactionsCalculator.calculateInteractions(gene, singleton(interactome), maxDegree, callback);
    
    return callback.getInteractions();
  }
  
  private static class InteractionsCallback extends SimpleInteractionsCalculusCallback {
    private final Set<GeneInteraction> interactions;
    
    public InteractionsCallback() {
      this.interactions = new HashSet<>();
    }
    
    public Stream<GeneInteraction> getInteractions() {
      return this.interactions.stream();
    }
    
    @Override
    public void interactionsCalculated(Stream<GeneInteraction> interactions) {
      interactions.forEach(this.interactions::add);
    }
  }
}
