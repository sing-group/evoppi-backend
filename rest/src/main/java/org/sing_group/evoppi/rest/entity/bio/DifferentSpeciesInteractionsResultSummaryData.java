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

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "different-species-interactions-result-summary", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "different-species-interactions-result-summary", description = "Result of an interaction different species query.")
public class DifferentSpeciesInteractionsResultSummaryData extends AbstractDifferentSpeciesInteractionsResultData implements Serializable {
  private static final long serialVersionUID = 1L;

  private URI interactions;
  
  private BlastQueryOptionsData blastQueryOptions;

  DifferentSpeciesInteractionsResultSummaryData() {}
  
  public DifferentSpeciesInteractionsResultSummaryData(
    String id,
    IdNameAndUri queryGene,
    int queryMaxDegree,
    IdNameAndUri referenceSpecies, IdNameAndUri targetSpecies,
    IdNameAndUri[] referenceInteractomes, IdNameAndUri[] targetInteractomes,
    URI interactions,
    int totalInteractions,
    ExecutionStatus status, 
    BlastQueryOptionsData blastQueryOptions
  ) {
    super(id, queryGene, queryMaxDegree, totalInteractions, status, referenceSpecies, targetSpecies, referenceInteractomes, targetInteractomes);
    
    this.interactions = interactions;
    this.blastQueryOptions = blastQueryOptions;
  }

  public URI getInteractions() {
    return interactions;
  }

  public void setInteractions(URI interactions) {
    this.interactions = interactions;
  }
  
  public BlastQueryOptionsData getBlastQueryOptions() {
    return blastQueryOptions;
  }

  public void setBlastQueryOptions(BlastQueryOptionsData blastQueryOptions) {
    this.blastQueryOptions = blastQueryOptions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(blastQueryOptions, interactions);
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
    DifferentSpeciesInteractionsResultSummaryData other = (DifferentSpeciesInteractionsResultSummaryData) obj;
    return Objects.equals(blastQueryOptions, other.blastQueryOptions)
      && Objects.equals(interactions, other.interactions);
  }
}
