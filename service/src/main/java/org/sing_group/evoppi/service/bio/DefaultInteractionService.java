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
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesInteractionsRequestEvent;
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
  private SameSpeciesInteractionsResultDAO sameInteractionsResultDao;
  
  @Inject
  private DifferentSpeciesInteractionsResultDAO differentInteractionsResultDao;
  
  @Inject
  private Event<SameSpeciesInteractionsRequestEvent> taskSameEvents;
  
  @Inject
  private Event<DifferentSpeciesInteractionsRequestEvent> taskDifferentEvents;
  
  @Override
  public boolean isSameSpeciesResult(String id) {
    return this.sameInteractionsResultDao.exists(id);
  }
  
  @Override
  public boolean isDifferentSpeciesResult(String id) {
    return this.differentInteractionsResultDao.exists(id);
  }
  
  @Override
  public SameSpeciesInteractionsResult getSameSpeciesResult(String id) {
    return this.sameInteractionsResultDao.get(id);
  }
  
  @Override
  public DifferentSpeciesInteractionsResult getDifferentSpeciesResult(String id) {
    return this.differentInteractionsResultDao.get(id);
  }
  
  @Override
  public SameSpeciesInteractionsResult findSameSpeciesInteractions(
    int geneId, int[] interactomeIds, int maxDegree, Function<String, String> resultReferenceBuilder
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    
    requireNonEmpty(interactomeIds, "At least one interactome id should be provided");
    
    this.checkSameSpecies(interactomeIds);
    
    final Gene gene = this.geneDao.getGene(geneId);
    final Collection<Interactome> interactomes = IntStream.of(interactomeIds)
      .mapToObj(this.interactomeDao::getInteractome)
    .collect(Collectors.toSet());
    
    final SameSpeciesInteractionsResult result = this.sameInteractionsResultDao.create(
      "Same species interactions",
      "Find same species interactions",
      resultReferenceBuilder,
      gene,
      maxDegree,
      interactomes
    );
    
    this.taskSameEvents.fire(new SameSpeciesInteractionsRequestEvent(geneId, interactomeIds, maxDegree, result.getId()));
    
    result.setScheduled();
    
    return result;
  }
  
  @Override
  public DifferentSpeciesInteractionsResult findDifferentSpeciesInteractions(
    int geneId, int[] referenceInteractomeIds, int[] targetInteractomeIds, BlastQueryOptions blastOptions,
    int maxDegree, Function<String, String> resultReferenceBuilder
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    
    final Gene gene = this.geneDao.getGene(geneId);
    
    if (!IntStream.of(referenceInteractomeIds).anyMatch(interactome -> gene.belongsToInteractome(interactome))) {
      throw new IllegalArgumentException("gene must belong to, at least, one reference interactomes");
    }
    
    checkDifferentSpecies(referenceInteractomeIds, targetInteractomeIds);
    
    final Collection<Interactome> referenceInteractomes = IntStream.of(referenceInteractomeIds)
      .mapToObj(this.interactomeDao::getInteractome)
    .collect(Collectors.toSet());
    
    final Collection<Interactome> targetInteractomes = IntStream.of(targetInteractomeIds)
      .mapToObj(this.interactomeDao::getInteractome)
    .collect(Collectors.toSet());
    
    final DifferentSpeciesInteractionsResult result = this.differentInteractionsResultDao.create(
      "Different species interactions",
      "Find different species interactions",
      resultReferenceBuilder,
      gene, referenceInteractomes, targetInteractomes, blastOptions, maxDegree
    );
    
    this.taskDifferentEvents.fire(new DifferentSpeciesInteractionsRequestEvent(
      geneId,
      referenceInteractomeIds,
      targetInteractomeIds,
      blastOptions,
      maxDegree,
      result.getId()
    ));
    
    result.setScheduled();
    
    return result;
  }
  
  @Override
  public String getSameSpeciesResultSingleFasta(String resultId, boolean includeVersionSuffix) {
    final SameSpeciesInteractionsResult result = this.getSameSpeciesResult(resultId);

    return createFastaFromInteractions(result.getInteractions(), includeVersionSuffix);
  }
  
  @Override
  public String getDifferentSpeciesResultSingleFasta(String resultId, boolean includeVersionSuffix) {
    final DifferentSpeciesInteractionsResult result = this.getDifferentSpeciesResult(resultId);
    
    final IntStream orthologIds = result.getBlastResults()
      .mapToInt(BlastResult::getSseqid)
      .distinct();

    return createFastaFromGeneIds(IntStream.concat(result.getReferenceGeneIds(), orthologIds), includeVersionSuffix);
  }
  
  @Override
  public String getSameSpeciesResultFasta(String resultId, int interactomeId, boolean includeVersionSuffix) {
    final SameSpeciesInteractionsResult result = this.getSameSpeciesResult(resultId);
    
    if (result.getQueryInteractomeIds().noneMatch(id -> id == interactomeId))
      throw new IllegalArgumentException("Invalid interactome id: " + interactomeId);

    final Stream<InteractionGroupResult> interactions = result.getInteractions()
      .filter(interaction -> interaction.getInteractomeIds().anyMatch(id -> id == interactomeId));
    
    return createFastaFromInteractions(interactions, includeVersionSuffix);
  }
  
  @Override
  public String getDifferentSpeciesResultFasta(String resultId, int interactomeId, boolean includeVersionSuffix) {
    final DifferentSpeciesInteractionsResult result = this.getDifferentSpeciesResult(resultId);
    
    if (result.getReferenceInteractomeIds().anyMatch(id -> id == interactomeId)) {
      return createFastaFromGeneIds(result.getReferenceGeneIds(), includeVersionSuffix);
    } else if (result.getTargetInteractomeIds().anyMatch(id -> id == interactomeId)) {
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
  
  private void checkSameSpecies(int ... interactomes) {
    requireNonEmpty(interactomes, "At least one interactome should be provided");
    
    if (!this.haveSameSpecies(interactomes)) {
      throw new IllegalArgumentException("Interactomes should belong to the same species");
    }
  }

  private void checkDifferentSpecies(int[] referenceInteractomes, int[] targetInteractomes) {
    requireNonEmpty(referenceInteractomes, "At least one reference interactome should be provided");
    requireNonEmpty(targetInteractomes, "At least one target interactome should be provided");
    
    if (!this.haveSameSpecies(referenceInteractomes)) {
      throw new IllegalArgumentException("Reference interactomes should belong to the same species");
    }
    
    if (!this.haveSameSpecies(targetInteractomes)) {
      throw new IllegalArgumentException("Target interactomes should belong to the same species");
    }
    
    final Species referenceSpecies = this.interactomeDao.getInteractome(referenceInteractomes[0]).getSpecies();
    final Species targetSpecies = this.interactomeDao.getInteractome(targetInteractomes[0]).getSpecies();
    
    if (referenceSpecies.equals(targetSpecies)) {
      throw new IllegalArgumentException("Reference and target interactomes should belong to differentSpecies");
    }
  }
}
