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

import java.util.Arrays;

public abstract class SameSpeciesCalculusEvent {
  private final int geneId;
  private final int[] interactomes;
  private final int maxDegree;
  private final int workId;
  private final int resultId;
  
  public SameSpeciesCalculusEvent(SameSpeciesCalculusEvent event) {
    this(event.getGeneId(), event.getInteractomes(), event.getMaxDegree(), event.getWorkId(), event.getResultId());
  }
  
  public SameSpeciesCalculusEvent(int geneId, int[] interactomes, int maxDegree, int workId, int resultId) {
    this.geneId = geneId;
    this.interactomes = interactomes;
    this.maxDegree = maxDegree;
    this.workId = workId;
    this.resultId = resultId;
  }

  public int getGeneId() {
    return geneId;
  }

  public int[] getInteractomes() {
    return interactomes;
  }

  public int getMaxDegree() {
    return maxDegree;
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
    result = prime * result + geneId;
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + maxDegree;
    result = prime * result + resultId;
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
    SameSpeciesCalculusEvent other = (SameSpeciesCalculusEvent) obj;
    if (geneId != other.geneId)
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (maxDegree != other.maxDegree)
      return false;
    if (resultId != other.resultId)
      return false;
    if (workId != other.workId)
      return false;
    return true;
  }
}