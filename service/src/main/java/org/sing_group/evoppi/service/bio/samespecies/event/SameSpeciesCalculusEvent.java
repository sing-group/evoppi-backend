/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
package org.sing_group.evoppi.service.bio.samespecies.event;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

public abstract class SameSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int geneId;
  private final int[] interactomes;
  private final int maxDegree;
  private final String workId;
  
  public SameSpeciesCalculusEvent(SameSpeciesCalculusEvent event) {
    this(event.getGeneId(), event.getInteractomes().toArray(), event.getMaxDegree(), event.getWorkId());
  }
  
  public SameSpeciesCalculusEvent(int geneId, int[] interactomes, int maxDegree, String workId) {
    this.geneId = geneId;
    this.interactomes = Arrays.copyOf(interactomes, interactomes.length);
    this.maxDegree = maxDegree;
    this.workId = workId;
  }

  public int getGeneId() {
    return geneId;
  }

  public IntStream getInteractomes() {
    return IntStream.of(interactomes);
  }

  public int getMaxDegree() {
    return maxDegree;
  }

  public String getWorkId() {
    return workId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneId;
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + maxDegree;
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
    SameSpeciesCalculusEvent other = (SameSpeciesCalculusEvent) obj;
    if (geneId != other.geneId)
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (maxDegree != other.maxDegree)
      return false;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }
}
