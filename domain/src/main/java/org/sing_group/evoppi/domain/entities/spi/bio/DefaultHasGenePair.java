/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.entities.spi.bio;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import org.sing_group.evoppi.domain.entities.bio.Gene;

class DefaultHasGenePair implements HasGenePair, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Gene geneA;
  private final Gene geneB;
  
  public DefaultHasGenePair(Gene geneA, Gene geneB) {
    this.geneA = requireNonNull(geneA, "geneA can't be null");
    this.geneB = requireNonNull(geneB, "geneB can't be null");
  }
  
  public DefaultHasGenePair(HasGenePair genePair) {
    this(genePair.getGeneA(), genePair.getGeneB());
  }

  @Override
  public Gene getGeneA() {
    return this.geneA;
  }
  
  @Override
  public Gene getGeneB() {
    return this.geneB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
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
    DefaultHasGenePair other = (DefaultHasGenePair) obj;
    if (geneA == null) {
      if (other.geneA != null)
        return false;
    } else if (!geneA.equals(other.geneA))
      return false;
    if (geneB == null) {
      if (other.geneB != null)
        return false;
    } else if (!geneB.equals(other.geneB))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "DefaultHasGenePair [geneA=" + geneA.getId() + ", geneB=" + geneB.getId() + "]";
  }
  
  
}
