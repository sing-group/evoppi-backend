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
package org.sing_group.evoppi.service.spi.bio.differentspecies;

import javax.ejb.Local;

import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesBlastAlignmentFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesBlastAlignmentStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesFastaCreationFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesFastaCreationStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesReferenceInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesReferenceInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesTargetInteractionsCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesTargetInteractionsCalculusStartedEvent;

@Local
public interface DifferentSpeciesInteractionEventManager {

  public void manageCalculusStarted(DifferentSpeciesCalculusStartedEvent event);

  public void manageReferenceInteractionsCalculusStarted(
    DifferentSpeciesReferenceInteractionsCalculusStartedEvent event
  );

  public void manageReferenceInteractionsCalculusFinished(
    DifferentSpeciesReferenceInteractionsCalculusFinishedEvent event
  );

  public void manageFastaCreationStarted(DifferentSpeciesFastaCreationStartedEvent event);

  public void manageFastaCreationFinished(DifferentSpeciesFastaCreationFinishedEvent event);

  public void manageBlastAlignmentStarted(DifferentSpeciesBlastAlignmentStartedEvent event);

  public void manageBlastAlignmentFinished(DifferentSpeciesBlastAlignmentFinishedEvent event);

  public void manageTargetInteractionsCalculusStarted(DifferentSpeciesTargetInteractionsCalculusStartedEvent event);

  public void manageTargetInteractionsCalculusFinished(DifferentSpeciesTargetInteractionsCalculusFinishedEvent event);

  public void manageCalculusFinishedEvent(DifferentSpeciesCalculusFinishedEvent event);
  
  public void manageFailure(DifferentSpeciesCalculusFailedEvent event);

}
