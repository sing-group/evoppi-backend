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

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesBlastAlignmentFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesBlastAlignmentStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesFastaCreationFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesFastaCreationStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesReferenceInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesReferenceInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesTargetInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesTargetInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesInteractionEventNotifier;

@Stateless
@PermitAll
public class DefaultDifferentSpeciesInteractionEventNotifier implements DifferentSpeciesInteractionEventNotifier {
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
  private Event<DifferentSpeciesCalculusFailedEvent> calculusFailedEvent;

  @Override
  public void notifyCalculusStarted(DifferentSpeciesCalculusEvent event) {
    this.calculusStartedEvent.fire(new DifferentSpeciesCalculusStartedEvent(event));
  }

  @Override
  public void notifyReferenceInteractionsCalculusStarted(DifferentSpeciesCalculusEvent event) {
    this.referenceInteractionsStartedEvent.fire(new DifferentSpeciesReferenceInteractionsCalculusStartedEvent(event));
  }

  @Override
  public void notifyReferenceInteractionsCalculusFinished(
    DifferentSpeciesCalculusEvent event, Set<Integer> referenceGeneIds,
    Collection<GeneInteraction> interactions
  ) {
    this.referenceInteractionsFinishedEvent
      .fire(new DifferentSpeciesReferenceInteractionsCalculusFinishedEvent(event, referenceGeneIds, interactions));
  }

  @Override
  public void notifyFastaCreationStarted(DifferentSpeciesCalculusEvent event) {
    this.fastaCreationStartedEvent.fire(new DifferentSpeciesFastaCreationStartedEvent(event));
  }

  @Override
  public void notifyFastaFinishedStarted(
    DifferentSpeciesInteractionsRequestEvent event, Path referenceFastaPath, Path targetFastaPath
  ) {
    this.fastaCreationFinishedEvent
      .fire(new DifferentSpeciesFastaCreationFinishedEvent(event, referenceFastaPath, targetFastaPath));
  }

  @Override
  public void notifyBlastAlignmentStarted(DifferentSpeciesCalculusEvent event) {
    this.blastAlignmentStartedEvent.fire(new DifferentSpeciesBlastAlignmentStartedEvent(event));
  }

  @Override
  public void notifyBlastAlignmentFinished(
    DifferentSpeciesCalculusEvent event, Collection<BlastResult> blastResults
  ) {
    this.blastAlignmentFinishedEvent.fire(new DifferentSpeciesBlastAlignmentFinishedEvent(event, blastResults));
  }

  @Override
  public void notifyTargetInteractionsCalculusStarted(
    DifferentSpeciesCalculusEvent event, Collection<Integer> geneIds
  ) {
    this.targetInteractionsStartedEvent
      .fire(new DifferentSpeciesTargetInteractionsCalculusStartedEvent(event, geneIds));
  }

  @Override
  public void notifyTargetInteractionsCalculusFinished(
    DifferentSpeciesCalculusEvent event, Collection<Integer> geneIds,
    Collection<GeneInteraction> interactions
  ) {
    this.targetInteractionsFinishedEvent
      .fire(new DifferentSpeciesTargetInteractionsCalculusFinishedEvent(event, geneIds, interactions));
  }

  @Override
  public void notifyCalculusFinished(DifferentSpeciesCalculusEvent event) {
    this.calculusFinishedEvent.fire(new DifferentSpeciesCalculusFinishedEvent(event));
  }

  @Override
  public void notifyCalculusFailed(
    DifferentSpeciesCalculusEvent event, String cause
  ) {
    this.calculusFailedEvent.fire(new DifferentSpeciesCalculusFailedEvent(event, cause));
  }

}
