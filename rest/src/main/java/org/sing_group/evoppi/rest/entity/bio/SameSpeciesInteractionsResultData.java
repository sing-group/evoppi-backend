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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "same-species-interaction-result", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "same-species-interaction-result", description = "Result of an interaction query.")
public class SameSpeciesInteractionsResultData extends AbstractSameSpeciesInteractionsResultData
  implements Serializable {
  private static final long serialVersionUID = 1L;

  private SameSpeciesInteractionsData interactions;

  SameSpeciesInteractionsResultData() {}

  public SameSpeciesInteractionsResultData(
    String id,
    IdNameAndUri queryGene,
    int queryMaxDegree,
    InteractionsResultFilteringOptionsData filteringOptions,
    IdNameAndUri species,
    IdNameAndUri[] interactomes,
    SameSpeciesInteractionsData interactions,
    int totalInteractions,
    ExecutionStatus status
  ) {
    super(id, queryGene, queryMaxDegree, totalInteractions, status, species, interactomes);

    this.interactions = interactions;
  }

  public SameSpeciesInteractionsData getInteractions() {
    return interactions;
  }

  public void setInteractions(SameSpeciesInteractionsData interactions) {
    this.interactions = interactions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((interactions == null) ? 0 : interactions.hashCode());
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
    SameSpeciesInteractionsResultData other = (SameSpeciesInteractionsResultData) obj;
    if (interactions == null) {
      if (other.interactions != null)
        return false;
    } else if (!interactions.equals(other.interactions))
      return false;
    return true;
  }
}
