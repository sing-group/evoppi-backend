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

import static java.util.stream.Collectors.toSet;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DifferentSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SameSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.execution.WorkDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.Work;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.storage.FastaOutputConfiguration;
import org.sing_group.evoppi.service.spi.storage.FastaWriter;

@Stateless
@PermitAll
public class DefaultInteractionService implements InteractionService {
  @Inject GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private FastaWriter fastaWriter;
  
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
    
    result.setScheduled();
    work.setScheduled();
    
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
    
    result.setScheduled();
    work.setScheduled();
    
    return work;
  }
  
  @Override
  public String getSameSpeciesResultSingleFasta(int resultId, boolean includeVersionSuffix) {
    final SameSpeciesInteractionsResult result = this.getSameSpeciesResult(resultId);

    return createFastaFromInteractions(result.getInteractions(), includeVersionSuffix);
  }
  
  @Override
  public String getDifferentSpeciesResultSingleFasta(int resultId, boolean includeVersionSuffix) {
    final DifferentSpeciesInteractionsResult result = this.getDifferentSpeciesResult(resultId);
    
    final IntStream orthologIds = result.getBlastResults()
      .mapToInt(BlastResult::getSseqid)
      .distinct();

    return createFastaFromGeneIds(IntStream.concat(result.getReferenceGeneIds(), orthologIds), includeVersionSuffix);
  }
  
  @Override
  public String getSameSpeciesResultFasta(int resultId, int interactomeId, boolean includeVersionSuffix) {
    final SameSpeciesInteractionsResult result = this.getSameSpeciesResult(resultId);
    
    if (result.getQueryInteractomeIds().noneMatch(id -> id == interactomeId))
      throw new IllegalArgumentException("Invalid interactome id: " + interactomeId);

    final Stream<InteractionGroupResult> interactions = result.getInteractions()
      .filter(interaction -> interaction.getInteractomeIds().anyMatch(id -> id == interactomeId));
    
    return createFastaFromInteractions(interactions, includeVersionSuffix);
  }
  
  @Override
  public String getDifferentSpeciesResultFasta(int resultId, int interactomeId, boolean includeVersionSuffix) {
    final DifferentSpeciesInteractionsResult result = this.getDifferentSpeciesResult(resultId);
    
    if (result.getReferenceInteractomeId() == interactomeId) {
      return createFastaFromGeneIds(result.getReferenceGeneIds(), includeVersionSuffix);
    } else if (result.getTargetInteractomeId() == interactomeId) {
      final IntStream orthologIds = result.getBlastResults()
        .mapToInt(BlastResult::getSseqid)
        .distinct();
      
      return createFastaFromGeneIds(orthologIds, includeVersionSuffix);
    } else {
      throw new IllegalArgumentException("Invalid interactome id: " + interactomeId);
    }
  }

  private String createFastaFromGeneIds(
    IntStream geneIds,
    boolean includeVersionSuffix
  ) {
    final Set<Gene> genes = geneIds
      .sorted()
      .mapToObj(this.geneDao::getGene)
    .collect(toSet());
    
    final StringWriter writer = new StringWriter();
    
    try {
      this.fastaWriter.createFasta(genes, writer, new FastaOutputConfiguration(includeVersionSuffix, true));
      
      return writer.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String createFastaFromInteractions(
    Stream<InteractionGroupResult> interactions,
    boolean includeVersionSuffix
  ) {
    final IntStream geneIds = interactions.flatMapToInt(InteractionGroupResult::getGeneIds);
    
    return createFastaFromGeneIds(geneIds, includeVersionSuffix);
  }
  
  private boolean haveSameSpecies(int... interactomes) {
    return IntStream.of(interactomes)
      .mapToObj(this.interactomeDao::getInteractome)
      .map(Interactome::getSpecies)
      .distinct()
    .count() == 1;
  }

}
