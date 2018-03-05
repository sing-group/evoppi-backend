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
package org.sing_group.evoppi.service.spi.bio.differentspecies.event;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

import javax.ejb.Local;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesCalculusEvent;
import org.sing_group.evoppi.service.bio.differentspecies.event.DifferentSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;

@Local
public interface DifferentSpeciesInteractionEventNotifier {

  public void notifyCalculusStarted(DifferentSpeciesCalculusEvent event);

  public void notifyReferenceInteractionsCalculusStarted(DifferentSpeciesCalculusEvent event);

  public void notifyReferenceInteractionsCalculusFinished(
    DifferentSpeciesCalculusEvent event, Set<Integer> referenceGeneIds,
    Collection<GeneInteraction> interactions
  );

  public void notifyFastaCreationStarted(DifferentSpeciesCalculusEvent event);

  public void notifyFastaFinishedStarted(
    DifferentSpeciesInteractionsRequestEvent event, Path referenceFastaPath, Path targetFastaPath
  );

  public void notifyBlastAlignmentStarted(DifferentSpeciesCalculusEvent event);

  public void notifyBlastAlignmentFinished(
    DifferentSpeciesCalculusEvent event, Collection<BlastResult> blastResults
  );

  public void notifyTargetInteractionsCalculusStarted(
    DifferentSpeciesCalculusEvent event, Collection<Integer> geneIds
  );

  public void notifyTargetInteractionsCalculusFinished(
    DifferentSpeciesCalculusEvent event, Collection<Integer> geneIds,
    Collection<GeneInteraction> interactions
  );

  public void notifyCalculusFinished(DifferentSpeciesCalculusEvent event);

  public void notifyCalculusFailed(
    DifferentSpeciesCalculusEvent event, String cause
  );

}
