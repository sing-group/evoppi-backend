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
package org.sing_group.evoppi.service.bio.samespecies.pipeline;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;

@Stateless
@PermitAll
public class DefaultSameSpeciesGeneInteractionsStep
implements SameSpeciesGeneInteractionsStep {
  
  private InteractomeDAO interactomeDao;
  private GeneDAO geneDao;
  private InteractionsCalculator interactionsCalculator;
  
  DefaultSameSpeciesGeneInteractionsStep() {}
  
  public DefaultSameSpeciesGeneInteractionsStep(
    InteractomeDAO interactomeDao, GeneDAO geneDao, InteractionsCalculator interactionsCalculator
  ) {
    this.setInteractomeDao(interactomeDao);
    this.setGeneDao(geneDao);
    this.setInteractomeDao(interactomeDao);
  }

  @Inject
  public void setInteractomeDao(InteractomeDAO interactomeDao) {
    this.interactomeDao = requireNonNull(interactomeDao);
  }
  
  @Inject
  public void setGeneDao(GeneDAO geneDao) {
    this.geneDao = requireNonNull(geneDao);
  }
  
  @Inject
  public void setInteractionsCalculator(InteractionsCalculator interactionsCalculator) {
    this.interactionsCalculator = requireNonNull(interactionsCalculator);
  }
  
  @Override
  public String getName() {
    return "Interactome comparison";
  }

  @Override
  public boolean isComplete(SameSpeciesGeneInteractionsContext context) {
    return context.getInteractions().isPresent();
  }

  @Override
  public SameSpeciesGeneInteractionsContext execute(SameSpeciesGeneInteractionsContext context) {
    final SameSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    
    final Gene gene = this.geneDao.getGene(configuration.getGeneId());
    final Collection<Interactome> interactomes = configuration.getInteractomes()
      .mapToObj(interactomeDao::getInteractome)
    .collect(toSet());

    final BridgeInteractionsCalculusCallback callback = new BridgeInteractionsCalculusCallback(context);
    
    this.interactionsCalculator.calculateInteractions(gene, interactomes, configuration.getMaxDegree(), callback);
    
    return callback.getContext();
  }
  
  private class BridgeInteractionsCalculusCallback implements InteractionsCalculusCallback {
    private SameSpeciesGeneInteractionsContext context;
    
    public SameSpeciesGeneInteractionsContext getContext() {
      return context;
    }
    
    public BridgeInteractionsCalculusCallback(SameSpeciesGeneInteractionsContext context) {
      this.context = context;
    }

    @Override
    public void calculusStarted() {}
    
    @Override
    public void interactionsCalculated(Stream<GeneInteraction> interactions) {
      this.context = this.context.setInteractions(interactions);
    }
    
    @Override
    public void calculusFinished() {}
  }
}
