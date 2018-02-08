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
package org.sing_group.evoppi.service.entity.bio;

import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class GeneInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  private final int geneAId;
  private final int geneBId;
  private final Map<Integer, Integer> interactomeDegrees;

  public GeneInteraction(int geneAId, int geneBId, Map<Integer, Integer> interactomeDegrees) {
    this.geneAId = geneAId;
    this.geneBId = geneBId;
    this.interactomeDegrees = new HashMap<>(interactomeDegrees);
  }

  public int getGeneAId() {
    return geneAId;
  }

  public int getGeneBId() {
    return geneBId;
  }
  
  public IntStream getGeneIds() {
    return IntStream.of(this.geneAId, this.geneBId);
  }

  public IntStream getInteractomeIds() {
    return this.interactomeDegrees.keySet().stream()
      .mapToInt(Integer::intValue);
  }
  
  public int getDegree(int interactomeId) {
    return this.interactomeDegrees.get(interactomeId);
  }
  
  public Map<Integer, Integer> getInteractomeDegrees() {
    return unmodifiableMap(interactomeDegrees);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneAId;
    result = prime * result + geneBId;
    result = prime * result + ((interactomeDegrees == null) ? 0 : interactomeDegrees.hashCode());
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
    GeneInteraction other = (GeneInteraction) obj;
    if (geneAId != other.geneAId)
      return false;
    if (geneBId != other.geneBId)
      return false;
    if (interactomeDegrees == null) {
      if (other.interactomeDegrees != null)
        return false;
    } else if (!interactomeDegrees.equals(other.interactomeDegrees))
      return false;
    return true;
  }
}
