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
package org.sing_group.evoppi.service.bio.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.ExecutionStatus;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class DifferentSpeciesTargetInteractionsCalculusFinishedEvent
  extends DifferentSpeciesCalculusEvent
  implements Serializable, WorkStepEvent {
  private static final long serialVersionUID = 1L;

  private final Collection<Integer> targetGeneIds;
  private final Collection<GeneInteraction> targetInteractions;

  public DifferentSpeciesTargetInteractionsCalculusFinishedEvent(
    DifferentSpeciesCalculusEvent event, Collection<Integer> targetGeneIds, Collection<GeneInteraction> targetInteractions
  ) {
    super(event);

    this.targetGeneIds = new HashSet<>(targetGeneIds);
    this.targetInteractions = new HashSet<>(targetInteractions);
  }

  public DifferentSpeciesTargetInteractionsCalculusFinishedEvent(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastQueryOptions, int maxDegree,
    int workId, int resultId, Collection<Integer> targetGeneIds, Collection<GeneInteraction> targetInteractions
  ) {
    super(geneId, referenceInteractome, targetInteractome, blastQueryOptions, maxDegree, workId, resultId);

    this.targetGeneIds = new HashSet<>(targetGeneIds);
    this.targetInteractions = new HashSet<>(targetInteractions);
  }

  public IntStream getTargetGeneIds() {
    return targetGeneIds.stream().mapToInt(Integer::intValue);
  }

  public Stream<GeneInteraction> getTargetInteractions() {
    return targetInteractions.stream();
  }

  @Override
  public String getDescription() {
    return String.format("%d interactions found for %d genes in interactome %d",
      this.getTargetInteractions().count(),
      this.getTargetGeneIds().count(),
      this.getTargetInteractome()
    );
  }

  @Override
  public double getProgress() {
    return 0.8d;
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return ExecutionStatus.RUNNING;
  }
}
