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
package org.sing_group.evoppi.service.bio;

import static java.util.Arrays.stream;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesBlastAlignmentFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesBlastAlignmentStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesBlastRequestEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculateTargetInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesFastaCreationFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesFastaCreationStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesReferenceInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesReferenceInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesTargetInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesTargetInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.BlastService;
import org.sing_group.evoppi.service.spi.bio.DifferentSpeciesInteractionService;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback.SimpleInteractionsCalculusCallback;
import org.sing_group.evoppi.service.spi.storage.GeneStorageService;
import org.sing_group.evoppi.service.spi.storage.GenomeStorageService;

@Stateless
@PermitAll
public class DefaultDifferentSpeciesInteractionService implements DifferentSpeciesInteractionService {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private Event<DifferentSpeciesBlastRequestEvent> blastRequestEvent;
  
  @Inject
  private Event<DifferentSpeciesCalculateTargetInteractionsRequestEvent> calculateTargetInteractionsEvent;
  
  @Inject
  private Event<DifferentSpeciesCalculusStartedEvent> calculusStartedEvent;
  
  @Inject
  private Event<DifferentSpeciesReferenceInteractionsCalculusStartedEvent> referenceInteractionsStartedEvent;
  
  @Inject
  private Event<DifferentSpeciesReferenceInteractionsCalculusFinishedEvent> referenceInteractionsFinishedEvent;
  
  @Inject
  private Event<DifferentSpeciesFastaCreationStartedEvent> fastaCreationStartedEvent;
  
  @Inject
  private Event<DifferentSpeciesFastaCreationFinishedEvent> fastaCreationFinishedEvent;
  
  @Inject
  private Event<DifferentSpeciesBlastAlignmentStartedEvent> blastAlignmentStartedEvent;
  
  @Inject
  private Event<DifferentSpeciesBlastAlignmentFinishedEvent> blastAlignmentFinishedEvent;
  
  @Inject
  private Event<DifferentSpeciesTargetInteractionsCalculusStartedEvent> targetInteractionsStartedEvent;
  
  @Inject
  private Event<DifferentSpeciesTargetInteractionsCalculusFinishedEvent> targetInteractionsFinishedEvent;
  
  @Inject
  private Event<DifferentSpeciesCalculusFinishedEvent> calculusFinishedEvent;
  
  @Inject
  private GeneStorageService geneStorageService;
  
  @Inject
  private GenomeStorageService genomeStorageService;
  
  @Inject
  private BlastService blastService;
  
  @Inject
  private InteractionsCalculator interactionsCalculator;
  
  @Override
  @Asynchronous
  public void calculateReferenceInteractions(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) DifferentSpeciesInteractionsRequestEvent event
  ) {
    try {
      this.notifyCalculusStarted(event);
      
      this.notifyReferenceInteractionsCalculusStarted(event);
      
      final Interactome targetInteractome = this.interactomeDao.getInteractome(event.getTargetInteractome());
      
      final Interactome referenceInteractome = this.interactomeDao.getInteractome(event.getReferenceInteractome());
      final Gene referenceGene = this.geneDao.getGene(event.getGeneId());
      final Set<GeneInteraction> referenceInteractions = this.getGeneInteractions(referenceGene, referenceInteractome, event.getMaxDegree())
        .collect(toSet());
      
      final Set<Gene> referenceGenes = referenceInteractions.stream()
        .flatMapToInt(GeneInteraction::getGeneIds)
        .distinct()
        .mapToObj(this.geneDao::getGene)
      .collect(toSet());
      
      final Set<Integer> referenceGeneIds = referenceGenes.stream()
        .map(Gene::getId)
      .collect(Collectors.toSet());
      
      this.notifyReferenceInteractionsCalculusFinished(event, referenceGeneIds, referenceInteractions);

      this.notifyFastaCreationStarted(event);
      
      final Path referenceFastaPath = this.geneStorageService.createFasta(referenceGenes);
      final Path targetFastaPath = this.genomeStorageService.getGenomePath(targetInteractome.getSpecies());
      
      this.notifyFastaFinishedStarted(event, referenceFastaPath, targetFastaPath);
      
      this.blastRequestEvent.fire(new DifferentSpeciesBlastRequestEvent(event, referenceFastaPath, targetFastaPath, referenceInteractions));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Asynchronous
  @TransactionAttribute(NOT_SUPPORTED)
  public void calculateBlast(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) DifferentSpeciesBlastRequestEvent event
  ) {
    try {
      this.notifyBlastAlignmentStarted(event);
      
      final Set<BlastResult> blastResults = this.blastService.blast(
        event.getTargetFastaPath(), event.getReferenceFastaPath(), event.getBlastQueryOptions()
      ).collect(toSet());
      
      this.notifyBlastAlignmentFinished(event, blastResults);
      
      this.calculateTargetInteractionsEvent.fire(new DifferentSpeciesCalculateTargetInteractionsRequestEvent(
        event, event.getReferenceInteractions().collect(toSet()), blastResults)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        Files.deleteIfExists(event.getReferenceFastaPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  @Asynchronous
  public void calculateTargetInteractions(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) DifferentSpeciesCalculateTargetInteractionsRequestEvent event
  ) {
    final Set<Integer> targtGeneIds = event.getBlastResults()
      .mapToInt(BlastResult::getSseqid)
      .distinct()
      .boxed()
    .collect(toSet());
    
    this.notifyTargetInteractionsCalculusStarted(event, targtGeneIds);
    
    final Interactome targetInteractome = this.interactomeDao.getInteractome(event.getTargetInteractome());
    
    final Gene[] targetGenes = targtGeneIds.stream()
      .map(this.geneDao::getGene)
    .toArray(Gene[]::new);
    
    final Set<GeneInteraction> targetInteractions = getDirectGeneInteractons(targetInteractome, targetGenes)
      .collect(toSet());
    
    this.notifyTargetInteractionsCalculusFinished(event, targtGeneIds, targetInteractions);
    
    this.notifyCalculusFinished(event);
  }
  
  private Stream<GeneInteraction> getDirectGeneInteractons(Interactome interactome, Gene ... genes) {
    final Set<Gene> genesSet = stream(genes).collect(toSet());
    final Predicate<Interaction> hasValidGenes = interaction -> 
      genesSet.contains(interaction.getGeneA()) && genesSet.contains(interaction.getGeneB());
    
    final int[] interactomeId = { interactome.getId() };
    
    return interactome.getInteractions()
      .filter(hasValidGenes)
      .map(interaction -> new GeneInteraction(
        interaction.getGeneA().getId(),
        interaction.getGeneB().getId(),
        interactomeId,
        1
      ));
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
    public void degreeCalculusFinished(int degree, Stream<GeneInteraction> interactions) {
      interactions.forEach(this.interactions::add);
    }
  }

  private void notifyCalculusStarted(DifferentSpeciesInteractionsRequestEvent event) {
    this.calculusStartedEvent.fire(new DifferentSpeciesCalculusStartedEvent(event));
  }

  private void notifyReferenceInteractionsCalculusStarted(DifferentSpeciesInteractionsRequestEvent event) {
    this.referenceInteractionsStartedEvent.fire(new DifferentSpeciesReferenceInteractionsCalculusStartedEvent(event));
  }

  private void notifyReferenceInteractionsCalculusFinished(
    DifferentSpeciesInteractionsRequestEvent event, Set<Integer> referenceGeneIds, Collection<GeneInteraction> interactions
  ) {
    this.referenceInteractionsFinishedEvent
      .fire(new DifferentSpeciesReferenceInteractionsCalculusFinishedEvent(event, referenceGeneIds, interactions));
  }

  private void notifyFastaCreationStarted(DifferentSpeciesInteractionsRequestEvent event) {
    this.fastaCreationStartedEvent.fire(new DifferentSpeciesFastaCreationStartedEvent(event));
  }
  
  private void notifyFastaFinishedStarted(
    DifferentSpeciesInteractionsRequestEvent event, Path referenceFastaPath, Path targetFastaPath
  ) {
    this.fastaCreationFinishedEvent
      .fire(new DifferentSpeciesFastaCreationFinishedEvent(event, referenceFastaPath, targetFastaPath));
  }

  private void notifyBlastAlignmentStarted(DifferentSpeciesBlastRequestEvent event) {
    this.blastAlignmentStartedEvent.fire(new DifferentSpeciesBlastAlignmentStartedEvent(event));
  }

  private void notifyBlastAlignmentFinished(
    DifferentSpeciesBlastRequestEvent event, Collection<BlastResult> blastResults
  ) {
    this.blastAlignmentFinishedEvent.fire(new DifferentSpeciesBlastAlignmentFinishedEvent(event, blastResults));
  }

  private void notifyTargetInteractionsCalculusStarted(
    DifferentSpeciesCalculateTargetInteractionsRequestEvent event, Collection<Integer> geneIds
  ) {
    this.targetInteractionsStartedEvent
      .fire(new DifferentSpeciesTargetInteractionsCalculusStartedEvent(event, geneIds));
  }

  private void notifyTargetInteractionsCalculusFinished(
    DifferentSpeciesCalculateTargetInteractionsRequestEvent event, Collection<Integer> geneIds,
    Collection<GeneInteraction> interactions
  ) {
    this.targetInteractionsFinishedEvent
      .fire(new DifferentSpeciesTargetInteractionsCalculusFinishedEvent(event, geneIds, interactions));
  }

  private void notifyCalculusFinished(DifferentSpeciesCalculateTargetInteractionsRequestEvent event) {
    this.calculusFinishedEvent.fire(new DifferentSpeciesCalculusFinishedEvent(event));
  }
}