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

import org.sing_group.evoppi.domain.dao.bio.execution.BlastQueryOptions;

public abstract class DifferentSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int geneId;
  private final int referenceInteractome;
  private final int targetInteractome;
  private final BlastQueryOptions blastQueryOptions;
  private final int maxDegree;
  private final int workId;
  private final int resultId;

  public DifferentSpeciesCalculusEvent(DifferentSpeciesCalculusEvent event) {
    this(
      event.getGeneId(),
      event.getReferenceInteractome(),
      event.getTargetInteractome(),
      event.getBlastQueryOptions(),
      event.getMaxDegree(),
      event.getWorkId(),
      event.getResultId()
    );
  }

  public DifferentSpeciesCalculusEvent(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastQueryOptions, int maxDegree,
    int workId, int resultId
  ) {
    this.geneId = geneId;
    this.referenceInteractome = referenceInteractome;
    this.targetInteractome = targetInteractome;
    this.blastQueryOptions = blastQueryOptions;
    this.maxDegree = maxDegree;
    this.workId = workId;
    this.resultId = resultId;
  }

  public int getGeneId() {
    return geneId;
  }

  public int getReferenceInteractome() {
    return referenceInteractome;
  }

  public int getTargetInteractome() {
    return targetInteractome;
  }

  public int getMaxDegree() {
    return maxDegree;
  }

  public BlastQueryOptions getBlastQueryOptions() {
    return blastQueryOptions;
  }

  public int getWorkId() {
    return workId;
  }

  public int getResultId() {
    return resultId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blastQueryOptions == null) ? 0 : blastQueryOptions.hashCode());
    result = prime * result + geneId;
    result = prime * result + maxDegree;
    result = prime * result + referenceInteractome;
    result = prime * result + resultId;
    result = prime * result + targetInteractome;
    result = prime * result + workId;
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
    if (referenceInteractome != other.referenceInteractome)
      return false;
    if (resultId != other.resultId)
      return false;
    if (targetInteractome != other.targetInteractome)
      return false;
    if (workId != other.workId)
      return false;
    return true;
  }
}
