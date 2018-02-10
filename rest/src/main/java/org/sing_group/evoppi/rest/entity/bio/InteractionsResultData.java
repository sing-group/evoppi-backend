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

import java.util.Arrays;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;

public abstract class InteractionsResultData {
  private String id;
  private int queryGene;
  private int queryMaxDegree;
  private InteractionResultData[] interactions;
  private ExecutionStatus status;

  public InteractionsResultData() {
    super();
  }

  public InteractionsResultData(
    String id, int queryGene, int queryMaxDegree, InteractionResultData[] interactions, ExecutionStatus status
  ) {
    this.id = id;
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
    this.interactions = interactions;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getQueryGene() {
    return queryGene;
  }

  public void setQueryGene(int queryGene) {
    this.queryGene = queryGene;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public void setQueryMaxDegree(int queryMaxDegree) {
    this.queryMaxDegree = queryMaxDegree;
  }

  public InteractionResultData[] getInteractions() {
    return interactions;
  }

  public void setInteractions(InteractionResultData[] interactions) {
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
    result = prime * result + queryGene;
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
    InteractionsResultData other = (InteractionsResultData) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (!Arrays.equals(interactions, other.interactions))
      return false;
    if (queryGene != other.queryGene)
      return false;
    if (queryMaxDegree != other.queryMaxDegree)
      return false;
    if (status != other.status)
      return false;
    return true;
  }
}
