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

import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesBlastAlignmentFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesBlastAlignmentStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesFastaCreationFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesFastaCreationStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesReferenceInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesReferenceInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesTargetInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.DifferentSpeciesTargetInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.spi.bio.DifferentSpeciesInteractionEventManager;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Stateless
@PermitAll
@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesInteractionPersistenceManager implements DifferentSpeciesInteractionEventManager {
  @Inject
  public InteractionService interactionsService;

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageCalculusStarted(@Observes DifferentSpeciesCalculusStartedEvent event) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());

    result.setRunning();
  }

  @Override
  public void manageReferenceInteractionsCalculusStarted(
    DifferentSpeciesReferenceInteractionsCalculusStartedEvent event
  ) {}

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageReferenceInteractionsCalculusFinished(
    @Observes DifferentSpeciesReferenceInteractionsCalculusFinishedEvent event
  ) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());

    event.getReferenceInteractions().forEach(
      interaction -> result.addInteraction(
        interaction.getGeneAId(),
        interaction.getGeneBId(),
        interaction.getInteractomeDegrees()
      )
    );
    
    result.setReferenceGeneIds(event.getReferenceGeneIds().boxed().collect(toSet()));
  }

  @Override
  public void manageFastaCreationStarted(DifferentSpeciesFastaCreationStartedEvent event) {}

  @Override
  public void manageFastaCreationFinished(DifferentSpeciesFastaCreationFinishedEvent event) {}

  @Override
  public void manageBlastAlignmentStarted(DifferentSpeciesBlastAlignmentStartedEvent event) {}

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageBlastAlignmentFinished(@Observes DifferentSpeciesBlastAlignmentFinishedEvent event) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());

    event.getBlastResults().forEach(result::addBlastResult);
  }

  @Override
  public void manageTargetInteractionsCalculusStarted(DifferentSpeciesTargetInteractionsCalculusStartedEvent event) {}

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageTargetInteractionsCalculusFinished(
    @Observes DifferentSpeciesTargetInteractionsCalculusFinishedEvent event
  ) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());

    event.getTargetInteractions().forEach(
      interaction -> result.addInteraction(
        interaction.getGeneAId(),
        interaction.getGeneBId(),
        interaction.getInteractomeDegrees()
      )
    );
    
    result.setTargetGeneIds(event.getTargetGeneIds().boxed().collect(toSet()));
  }

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageCalculusFinishedEvent(@Observes DifferentSpeciesCalculusFinishedEvent event) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());

    result.setFinished();
  }

  @Transactional(REQUIRES_NEW)
  @Override
  public void manageFailure(@Observes DifferentSpeciesCalculusFailedEvent event) {
    final DifferentSpeciesInteractionsResult result =
      this.interactionsService.getDifferentSpeciesResult(event.getResultId());
    
    result.setFailed(event.getCause());
  }

}
