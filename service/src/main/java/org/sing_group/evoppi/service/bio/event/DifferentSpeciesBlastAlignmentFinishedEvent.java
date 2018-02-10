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
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class DifferentSpeciesBlastAlignmentFinishedEvent
extends DifferentSpeciesCalculusEvent
implements Serializable, WorkStepEvent {
  private static final long serialVersionUID = 1L;

  private final Collection<BlastResult> blastResults;

  public DifferentSpeciesBlastAlignmentFinishedEvent(
    DifferentSpeciesCalculusEvent event, Collection<BlastResult> blastResults
  ) {
    super(event);

    this.blastResults = new HashSet<>(blastResults);
  }
  
  public DifferentSpeciesBlastAlignmentFinishedEvent(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastQueryOptions, int maxDegree,
    String workId, String resultId, Collection<BlastResult> blastResults
  ) {
    super(geneId, referenceInteractome, targetInteractome, blastQueryOptions, maxDegree, workId, resultId);
    
    this.blastResults = blastResults;
  }
  
  public Stream<BlastResult> getBlastResults() {
    return blastResults.stream();
  }

  @Override
  public String getDescription() {
    return "BLAST alignment finished";
  }

  @Override
  public double getProgress() {
    return 0.6d;
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return ExecutionStatus.RUNNING;
  }
}
