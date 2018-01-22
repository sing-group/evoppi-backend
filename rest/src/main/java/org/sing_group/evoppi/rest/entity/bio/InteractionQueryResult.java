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

  private long queryGene;

  private long[] queryInteractomes;
  
  private int queryMaxDegree;

  private IdAndUri[] interactomes;

  private IdAndUri[] genes;

  private InteractionResultData[] interactions;

  private ExecutionStatus status;

  public InteractionQueryResult(
    IdAndUri id,
    long queryGene,
    long[] queryInteractomes,
    int queryMaxDegree,
    IdAndUri[] interactomes,
    IdAndUri[] genes,
    InteractionResultData[] interactions,
    ExecutionStatus status
  ) {
    this.id = id;
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
    this.queryInteractomes = queryInteractomes;
    this.interactomes = interactomes;
    this.genes = genes;
    this.interactions = interactions;
    this.status = status;
  }

  public IdAndUri getId() {
    return id;
  }

  public void setId(IdAndUri id) {
    this.id = id;
  }

  public long getQueryGene() {
    return queryGene;
  }

  public void setQueryGene(long queryGene) {
    this.queryGene = queryGene;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public void setQueryMaxDegree(int queryMaxDegree) {
    this.queryMaxDegree = queryMaxDegree;
  }

  public long[] getQueryInteractomes() {
    return queryInteractomes;
  }

  public void setQueryInteractomes(long[] queryInteractomes) {
    this.queryInteractomes = queryInteractomes;
  }

  public InteractionResultData[] getInteractions() {
    return interactions;
  }

  public void setInteractions(InteractionResultData[] interactions) {
    this.interactions = interactions;
  }
  
  public IdAndUri[] getInteractomes() {
    return interactomes;
  }

  public void setInteractomes(IdAndUri[] interactomes) {
    this.interactomes = interactomes;
  }

  public IdAndUri[] getGenes() {
    return genes;
  }

  public void setGenes(IdAndUri[] genes) {
    this.genes = genes;
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
    result = prime * result + Arrays.hashCode(genes);
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + Arrays.hashCode(interactions);
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + (int) (queryGene ^ (queryGene >>> 32));
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
    if (!Arrays.equals(genes, other.genes))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (!Arrays.equals(interactions, other.interactions))
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (queryGene != other.queryGene)
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
