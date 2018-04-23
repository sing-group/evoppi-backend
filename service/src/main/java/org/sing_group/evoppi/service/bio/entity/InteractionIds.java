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
package org.sing_group.evoppi.service.bio.entity;

import java.util.stream.IntStream;

public class InteractionIds {
  private final int interactomeId;
  private final int geneA;
  private final int geneB;

  public InteractionIds(int interactomeId, int geneA, int geneB) {
    this.interactomeId = interactomeId;
    this.geneA = geneA;
    this.geneB = geneB;
  }
  
  public int getInteractomeId() {
    return interactomeId;
  }

  public int getGeneA() {
    return geneA;
  }

  public int getGeneB() {
    return geneB;
  }

  public IntStream getGenes() {
    return IntStream.of(this.geneA, this.geneB);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneA;
    result = prime * result + geneB;
    result = prime * result + interactomeId;
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
    InteractionIds other = (InteractionIds) obj;
    if (geneA != other.geneA)
      return false;
    if (geneB != other.geneB)
      return false;
    if (interactomeId != other.interactomeId)
      return false;
    return true;
  }
}
