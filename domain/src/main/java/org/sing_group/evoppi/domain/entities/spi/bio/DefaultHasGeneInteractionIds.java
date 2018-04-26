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

import java.io.Serializable;

class DefaultHasGeneInteractionIds implements HasGeneInteractionIds, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int interactomeId;
  private final int geneAId;
  private final int geneBId;

  public DefaultHasGeneInteractionIds(HasGeneInteractionIds interactionIds) {
    this(interactionIds.getInteractomeId(), interactionIds);
  }

  public DefaultHasGeneInteractionIds(int interactomeId, HasGenePairIds genePairIds) {
    this.interactomeId = interactomeId;
    this.geneAId = genePairIds.getGeneAId();
    this.geneBId = genePairIds.getGeneBId();
  }

  @Override
  public int getInteractomeId() {
    return this.interactomeId;
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
    DefaultHasGeneInteractionIds other = (DefaultHasGeneInteractionIds) obj;
    if (geneAId != other.geneAId)
      return false;
    if (geneBId != other.geneBId)
      return false;
    if (interactomeId != other.interactomeId)
      return false;
    return true;
  }
}
