/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.function.Function;
import java.util.stream.IntStream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DifferentSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SameSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.execution.WorkDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.Work;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Stateless
@PermitAll
public class DefaultInteractionService implements InteractionService {
  @Inject GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private WorkDAO workDao;
  
  @Inject
  private SameSpeciesInteractionsResultDAO sameInteractionsResultDao;
  
  @Inject
  private DifferentSpeciesInteractionsResultDAO differentInteractionsResultDao;
  
  @Inject
  private Event<SameSpeciesInteractionsRequestEvent> taskSameEvents;
  
  @Inject
  private Event<DifferentSpeciesInteractionsRequestEvent> taskDifferentEvents;
  
  @Override
  public boolean isSameSpeciesResult(int id) {
    return this.sameInteractionsResultDao.exists(id);
  }
  
  @Override
  public boolean isDifferentSpeciesResult(int id) {
    return this.differentInteractionsResultDao.exists(id);
  }
  
  @Override
  public SameSpeciesInteractionsResult getSameSpeciesResult(int id) {
    return this.sameInteractionsResultDao.get(id);
  }
  
  @Override
  public DifferentSpeciesInteractionsResult getDifferentSpeciesResult(int id) {
    return this.differentInteractionsResultDao.get(id);
  }
  
  @Override
  public Work findSameSpeciesInteractions(
    int geneId, int[] interactomes, int maxDegree, Function<Integer, String> resultReferenceBuilder
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    if (!this.haveSameSpecies(interactomes)) {
      throw new IllegalArgumentException("All the interactomes must belong to the same species");
    }
    
    final SameSpeciesInteractionsResult result = this.sameInteractionsResultDao.create(geneId, maxDegree, interactomes);
    
    final Work work = this.workDao.createNew(
      "Same species interactions",
      "Find same species interactions",
      resultReferenceBuilder.apply(result.getId())
    );
    
    this.taskSameEvents.fire(new SameSpeciesInteractionsRequestEvent(geneId, interactomes, maxDegree, work.getId(), result.getId()));
    
    result.scheduled();
    
    return work;
  }
  
  @Override
  public Work findDifferentSpeciesInteractions(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastOptions,
    int maxDegree, Function<Integer, String> resultReferenceBuilder
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    
    final Gene gene = this.geneDao.getGene(geneId);
    
    if (!gene.belongsToInteractome(referenceInteractome))
      throw new IllegalArgumentException("gene must belong to referenceInteractome");
    
    if (this.haveSameSpecies(referenceInteractome, targetInteractome)) {
      throw new IllegalArgumentException("All the interactomes must belong to the different species");
    }
    
    final DifferentSpeciesInteractionsResult result = this.differentInteractionsResultDao.create(
      geneId, referenceInteractome, targetInteractome, blastOptions, maxDegree
    );
    
    final Work work = this.workDao.createNew(
      "Different species interactions",
      "Find different species interactions",
      resultReferenceBuilder.apply(result.getId())
    );
    
    this.taskDifferentEvents.fire(new DifferentSpeciesInteractionsRequestEvent(
      geneId, referenceInteractome, targetInteractome, blastOptions, maxDegree, work.getId(), result.getId())
    );
    
    result.scheduled();
    
    return work;
  }
  
  private boolean haveSameSpecies(int... interactomes) {
    return IntStream.of(interactomes)
      .mapToObj(this.interactomeDao::getInteractome)
      .map(Interactome::getSpecies)
      .distinct()
    .count() == 1;
  }

}
