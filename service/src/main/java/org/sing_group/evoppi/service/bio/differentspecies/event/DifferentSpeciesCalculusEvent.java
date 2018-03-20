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
package org.sing_group.evoppi.service.bio.differentspecies.event;

import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.IntStream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;

public abstract class DifferentSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int geneId;
  private final Set<Integer> referenceInteractomes;
  private final Set<Integer> targetInteractomes;
  private final BlastQueryOptions blastQueryOptions;
  private final int maxDegree;
  private final String workId;
  private final String resultId;

  public DifferentSpeciesCalculusEvent(DifferentSpeciesCalculusEvent event) {
    this(
      event.getGeneId(),
      event.getReferenceInteractomes().boxed().collect(toSet()),
      event.getTargetInteractomes().boxed().collect(toSet()),
      event.getBlastQueryOptions(),
      event.getMaxDegree(),
      event.getWorkId(),
      event.getResultId()
    );
  }

  public DifferentSpeciesCalculusEvent(
    int geneId,
    Set<Integer> referenceInteractomes,
    Set<Integer> targetInteractomes,
    BlastQueryOptions blastQueryOptions,
    int maxDegree, String workId, String resultId
  ) {
    this.geneId = geneId;
    this.referenceInteractomes = referenceInteractomes;
    this.targetInteractomes = targetInteractomes;
    this.blastQueryOptions = blastQueryOptions;
    this.maxDegree = maxDegree;
    this.workId = workId;
    this.resultId = resultId;
  }

  public int getGeneId() {
    return geneId;
  }

  public IntStream getReferenceInteractomes() {
    return referenceInteractomes.stream().mapToInt(Integer::intValue);
  }

  public IntStream getTargetInteractomes() {
    return targetInteractomes.stream().mapToInt(Integer::intValue);
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

  public String getResultId() {
    return resultId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blastQueryOptions == null) ? 0 : blastQueryOptions.hashCode());
    result = prime * result + geneId;
    result = prime * result + maxDegree;
    result = prime * result + ((referenceInteractomes == null) ? 0 : referenceInteractomes.hashCode());
    result = prime * result + ((resultId == null) ? 0 : resultId.hashCode());
    result = prime * result + ((targetInteractomes == null) ? 0 : targetInteractomes.hashCode());
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
    if (referenceInteractomes == null) {
      if (other.referenceInteractomes != null)
        return false;
    } else if (!referenceInteractomes.equals(other.referenceInteractomes))
      return false;
    if (resultId == null) {
      if (other.resultId != null)
        return false;
    } else if (!resultId.equals(other.resultId))
      return false;
    if (targetInteractomes == null) {
      if (other.targetInteractomes != null)
        return false;
    } else if (!targetInteractomes.equals(other.targetInteractomes))
      return false;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }
}
