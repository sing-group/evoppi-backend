/*-
 * #%L
 * REST
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

package org.sing_group.evoppi.rest.entity.bio;

import java.util.Arrays;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;

public abstract class AbstractSameSpeciesInteractionsResultData
  extends InteractionsResultData {

  private IdNameAndUri species;

  private IdNameAndUri[] interactomes;

  AbstractSameSpeciesInteractionsResultData() {}

  public AbstractSameSpeciesInteractionsResultData(
    String id, IdNameAndUri queryGene, int queryMaxDegree, int totalInteractions, ExecutionStatus status, IdNameAndUri species,
    IdNameAndUri[] interactomes
  ) {
    super(id, queryGene, queryMaxDegree, totalInteractions, status);
    this.species = species;
    this.interactomes = interactomes;
  }

  public IdNameAndUri getSpecies() {
    return species;
  }

  public void setSpecies(IdNameAndUri species) {
    this.species = species;
  }

  public IdNameAndUri[] getInteractomes() {
    return interactomes;
  }

  public void setInteractomes(IdNameAndUri[] interactomes) {
    this.interactomes = interactomes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + ((species == null) ? 0 : species.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractSameSpeciesInteractionsResultData other = (AbstractSameSpeciesInteractionsResultData) obj;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }

}
