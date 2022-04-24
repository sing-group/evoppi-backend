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

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;

public abstract class InteractionsResultData {
  private String id;
  private IdNameAndUri queryGene;
  private int queryMaxDegree;
  private int totalInteractions;
  private ExecutionStatus status;

  InteractionsResultData() {}
  
  public InteractionsResultData(
    String id, IdNameAndUri queryGene, int queryMaxDegree, int totalInteractions, ExecutionStatus status
  ) {
    this.id = id;
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
    this.totalInteractions = totalInteractions;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IdNameAndUri getQueryGene() {
    return queryGene;
  }

  public void setQueryGene(IdNameAndUri queryGene) {
    this.queryGene = queryGene;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public void setQueryMaxDegree(int queryMaxDegree) {
    this.queryMaxDegree = queryMaxDegree;
  }

  public int getTotalInteractions() {
    return totalInteractions;
  }

  public void setTotalInteractions(int totalInteractions) {
    this.totalInteractions = totalInteractions;
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
    result = prime * result + ((queryGene == null) ? 0 : queryGene.hashCode());
    result = prime * result + queryMaxDegree;
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + totalInteractions;
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
    if (queryGene == null) {
      if (other.queryGene != null)
        return false;
    } else if (!queryGene.equals(other.queryGene))
      return false;
    if (queryMaxDegree != other.queryMaxDegree)
      return false;
    if (status != other.status)
      return false;
    if (totalInteractions != other.totalInteractions)
      return false;
    return true;
  }
  
}
