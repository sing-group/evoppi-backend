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

import org.sing_group.evoppi.domain.entities.bio.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.user.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interaction-query-result", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interaction-query-result", description = "Result of an interaction query.")
public class InteractionQueryResult implements Serializable {
  private static final long serialVersionUID = 1L;

  private IdAndUri id;

  private IdAndUri queryGene;

  private int queryMaxDegree;

  private IdAndUri[] queryInteractomes;

  private InteractionData[] interactions;

  private ExecutionStatus status;

  public InteractionQueryResult(
    IdAndUri id, IdAndUri queryGene, int queryMaxDegree, IdAndUri[] queryInteractomes, InteractionData[] interactions,
    ExecutionStatus status
  ) {
    this.id = id;
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
    this.queryInteractomes = queryInteractomes;
    this.interactions = interactions;
    this.status = status;
  }

  public IdAndUri getId() {
    return id;
  }

  public void setId(IdAndUri id) {
    this.id = id;
  }

  public IdAndUri getQueryGene() {
    return queryGene;
  }

  public void setQueryGene(IdAndUri queryGene) {
    this.queryGene = queryGene;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public void setQueryMaxDegree(int queryMaxDegree) {
    this.queryMaxDegree = queryMaxDegree;
  }

  public IdAndUri[] getQueryInteractomes() {
    return queryInteractomes;
  }

  public void setQueryInteractomes(IdAndUri[] queryInteractomes) {
    this.queryInteractomes = queryInteractomes;
  }

  public InteractionData[] getInteractions() {
    return interactions;
  }

  public void setInteractions(InteractionData[] interactions) {
    this.interactions = interactions;
  }

  public ExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(ExecutionStatus status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + Arrays.hashCode(interactions);
    result = prime * result + ((queryGene == null) ? 0 : queryGene.hashCode());
    result = prime * result + Arrays.hashCode(queryInteractomes);
    result = prime * result + queryMaxDegree;
    result = prime * result + ((status == null) ? 0 : status.hashCode());
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
    InteractionQueryResult other = (InteractionQueryResult) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (!Arrays.equals(interactions, other.interactions))
      return false;
    if (queryGene == null) {
      if (other.queryGene != null)
        return false;
    } else if (!queryGene.equals(other.queryGene))
      return false;
    if (!Arrays.equals(queryInteractomes, other.queryInteractomes))
      return false;
    if (queryMaxDegree != other.queryMaxDegree)
      return false;
    if (status != other.status)
      return false;
    return true;
  }
}
