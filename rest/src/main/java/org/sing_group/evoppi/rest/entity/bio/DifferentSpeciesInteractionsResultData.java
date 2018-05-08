/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "different-species-interactions-result", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "different-species-interactions-result", description = "Result of an interaction different species query."
)
public class DifferentSpeciesInteractionsResultData extends InteractionsResultData implements Serializable {
  private static final long serialVersionUID = 1L;

  private IdAndUri[] referenceInteractomes;

  private IdAndUri[] targetInteractomes;

  private DifferentSpeciesInteractionsData interactions;

  DifferentSpeciesInteractionsResultData() {}

  public DifferentSpeciesInteractionsResultData(
    String id,
    int queryGene,
    int queryMaxDegree,
    InteractionsResultFilteringOptionsData filteringOptions,
    IdAndUri[] referenceInteractomes,
    IdAndUri[] targetInteractomes,
    DifferentSpeciesInteractionsData interactions,
    int totalInteractions,
    ExecutionStatus status
  ) {
    super(id, queryGene, queryMaxDegree, totalInteractions, status);
    
    this.referenceInteractomes = referenceInteractomes;
    this.targetInteractomes = targetInteractomes;

    this.interactions = interactions;
  }

  public IdAndUri[] getReferenceInteractomes() {
    return referenceInteractomes;
  }

  public void setReferenceInteractomes(IdAndUri[] referenceInteractome) {
    this.referenceInteractomes = referenceInteractome;
  }

  public IdAndUri[] getTargetInteractomes() {
    return targetInteractomes;
  }

  public void setTargetInteractomes(IdAndUri[] targetInteractome) {
    this.targetInteractomes = targetInteractome;
  }

  public DifferentSpeciesInteractionsData getInteractions() {
    return interactions;
  }

  public void setInteractions(DifferentSpeciesInteractionsData interactions) {
    this.interactions = interactions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((interactions == null) ? 0 : interactions.hashCode());
    result = prime * result + Arrays.hashCode(referenceInteractomes);
    result = prime * result + Arrays.hashCode(targetInteractomes);
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
    DifferentSpeciesInteractionsResultData other = (DifferentSpeciesInteractionsResultData) obj;
    if (interactions == null) {
      if (other.interactions != null)
        return false;
    } else if (!interactions.equals(other.interactions))
      return false;
    if (!Arrays.equals(referenceInteractomes, other.referenceInteractomes))
      return false;
    if (!Arrays.equals(targetInteractomes, other.targetInteractomes))
      return false;
    return true;
  }
}
