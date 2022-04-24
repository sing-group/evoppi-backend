/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.spi.bio;

import java.io.Serializable;

class DefaultHasGenePairIds implements HasGenePairIds, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int geneAId;
  private final int geneBId;
  
  public DefaultHasGenePairIds(int geneAId, int geneBId) {
    this.geneAId = geneAId;
    this.geneBId = geneBId;
  }
  
  public DefaultHasGenePairIds(HasGenePairIds genePairIds) {
    this(genePairIds.getGeneAId(), genePairIds.getGeneBId());
  }

  @Override
  public int getGeneAId() {
    return this.geneAId;
  }
  
  @Override
  public int getGeneBId() {
    return this.geneBId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneAId;
    result = prime * result + geneBId;
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
    DefaultHasGenePairIds other = (DefaultHasGenePairIds) obj;
    if (geneAId != other.geneAId)
      return false;
    if (geneBId != other.geneBId)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "DefaultHasGenePairIds [geneAId=" + geneAId + ", geneBId=" + geneBId + "]";
  }
}
