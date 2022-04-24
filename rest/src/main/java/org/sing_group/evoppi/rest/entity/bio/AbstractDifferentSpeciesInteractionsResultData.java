/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.bio;

import java.util.Arrays;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;

public abstract class AbstractDifferentSpeciesInteractionsResultData
  extends InteractionsResultData {

  private IdNameAndUri referenceSpecies;

  private IdNameAndUri targetSpecies;

  private IdNameAndUri[] referenceInteractomes;

  private IdNameAndUri[] targetInteractomes;

  AbstractDifferentSpeciesInteractionsResultData() {}

  public AbstractDifferentSpeciesInteractionsResultData(
    String id, IdNameAndUri queryGene, int queryMaxDegree, int totalInteractions, ExecutionStatus status,
    IdNameAndUri referenceSpecies, IdNameAndUri targetSpecies,
    IdNameAndUri[] referenceInteractomes, IdNameAndUri[] targetInteractomes
  ) {
    super(id, queryGene, queryMaxDegree, totalInteractions, status);

    this.referenceSpecies = referenceSpecies;
    this.targetSpecies = targetSpecies;
    this.referenceInteractomes = referenceInteractomes;
    this.targetInteractomes = targetInteractomes;
  }

  public IdNameAndUri getReferenceSpecies() {
    return referenceSpecies;
  }

  public void setReferenceSpecies(IdNameAndUri referenceSpecies) {
    this.referenceSpecies = referenceSpecies;
  }

  public IdNameAndUri getTargetSpecies() {
    return targetSpecies;
  }

  public void setTargetSpecies(IdNameAndUri targetSpecies) {
    this.targetSpecies = targetSpecies;
  }

  public IdNameAndUri[] getReferenceInteractomes() {
    return referenceInteractomes;
  }

  public void setReferenceInteractomes(IdNameAndUri[] referenceInteractomes) {
    this.referenceInteractomes = referenceInteractomes;
  }

  public IdNameAndUri[] getTargetInteractomes() {
    return targetInteractomes;
  }

  public void setTargetInteractomes(IdNameAndUri[] targetInteractomes) {
    this.targetInteractomes = targetInteractomes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(referenceInteractomes);
    result = prime * result + ((referenceSpecies == null) ? 0 : referenceSpecies.hashCode());
    result = prime * result + Arrays.hashCode(targetInteractomes);
    result = prime * result + ((targetSpecies == null) ? 0 : targetSpecies.hashCode());
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
    AbstractDifferentSpeciesInteractionsResultData other = (AbstractDifferentSpeciesInteractionsResultData) obj;
    if (!Arrays.equals(referenceInteractomes, other.referenceInteractomes))
      return false;
    if (referenceSpecies == null) {
      if (other.referenceSpecies != null)
        return false;
    } else if (!referenceSpecies.equals(other.referenceSpecies))
      return false;
    if (!Arrays.equals(targetInteractomes, other.targetInteractomes))
      return false;
    if (targetSpecies == null) {
      if (other.targetSpecies != null)
        return false;
    } else if (!targetSpecies.equals(other.targetSpecies))
      return false;
    return true;
  }
}
