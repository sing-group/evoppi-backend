/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

package org.sing_group.evoppi.service.bio.differentspecies.event;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;

public abstract class DifferentSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int geneId;
  private final int[] referenceInteractomes;
  private final int[] targetInteractomes;
  private final BlastQueryOptions blastQueryOptions;
  private final int maxDegree;
  private final String workId;

  public DifferentSpeciesCalculusEvent(DifferentSpeciesCalculusEvent event) {
    this(
      event.getGeneId(),
      event.getReferenceInteractomes().toArray(),
      event.getTargetInteractomes().toArray(),
      event.getBlastQueryOptions(),
      event.getMaxDegree(),
      event.getWorkId()
    );
  }

  public DifferentSpeciesCalculusEvent(
    int geneId,
    int[] referenceInteractomes,
    int[] targetInteractomes,
    BlastQueryOptions blastQueryOptions,
    int maxDegree, String workId
  ) {
    this.geneId = geneId;
    this.referenceInteractomes = Arrays.copyOf(referenceInteractomes, referenceInteractomes.length);
    this.targetInteractomes = Arrays.copyOf(targetInteractomes, targetInteractomes.length);
    this.blastQueryOptions = blastQueryOptions;
    this.maxDegree = maxDegree;
    this.workId = workId;
  }

  public int getGeneId() {
    return geneId;
  }

  public IntStream getReferenceInteractomes() {
    return IntStream.of(this.referenceInteractomes);
  }

  public IntStream getTargetInteractomes() {
    return IntStream.of(this.targetInteractomes);
  }

  public int getMaxDegree() {
    return maxDegree;
  }

  public BlastQueryOptions getBlastQueryOptions() {
    return blastQueryOptions;
  }

  public String getWorkId() {
    return workId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blastQueryOptions == null) ? 0 : blastQueryOptions.hashCode());
    result = prime * result + geneId;
    result = prime * result + maxDegree;
    result = prime * result + Arrays.hashCode(referenceInteractomes);
    result = prime * result + Arrays.hashCode(targetInteractomes);
    result = prime * result + ((workId == null) ? 0 : workId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DifferentSpeciesCalculusEvent other = (DifferentSpeciesCalculusEvent) obj;
    if (blastQueryOptions == null) {
      if (other.blastQueryOptions != null)
        return false;
    } else if (!blastQueryOptions.equals(other.blastQueryOptions))
      return false;
    if (geneId != other.geneId)
      return false;
    if (maxDegree != other.maxDegree)
      return false;
    if (!Arrays.equals(referenceInteractomes, other.referenceInteractomes))
      return false;
    if (!Arrays.equals(targetInteractomes, other.targetInteractomes))
      return false;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }
}
