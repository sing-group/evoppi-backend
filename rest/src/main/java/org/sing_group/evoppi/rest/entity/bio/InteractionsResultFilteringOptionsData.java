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

import static org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultField.INTERACTOME;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultField;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactions-result-filtering-options", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactions-result-filtering-options", description = "Filtering options for the interactions.")
public class InteractionsResultFilteringOptionsData {
  private Integer page;
  private Integer pageSize;
  private InteractionGroupResultField orderField;
  private SortDirection sortDirection;
  private Integer interactomeId;

  InteractionsResultFilteringOptionsData() {}
  
  public InteractionsResultFilteringOptionsData(
    Integer page, Integer pageSize,
    InteractionGroupResultField orderField, SortDirection sortDirection,
    Integer interactomeId
  ) {
    if (page == null ^ pageSize == null) {
      throw new IllegalArgumentException("page and pageSize must be used together");
    }
    
    if (orderField == null ^ sortDirection == null) {
      throw new IllegalArgumentException("orderField and sortDirection must be used together");
    } else {
      if (orderField == INTERACTOME ^ interactomeId != null) {
        throw new IllegalArgumentException("interactomeId must be non null when orderField is INTERACTOME");
      }
    }
    
    this.page = page;
    this.pageSize = pageSize;
    this.orderField = orderField;
    this.sortDirection = sortDirection;
    this.interactomeId = interactomeId;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public InteractionGroupResultField getOrderField() {
    return orderField;
  }

  public void setOrderField(InteractionGroupResultField orderField) {
    this.orderField = orderField;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public Integer getInteractomeId() {
    return interactomeId;
  }

  public void setInteractomeId(Integer interactomeId) {
    this.interactomeId = interactomeId;
  }
  
  public boolean hasPagination() {
    return this.page != null && this.pageSize != null;
  }
  
  public boolean hasOrder() {
    return this.orderField != null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((interactomeId == null) ? 0 : interactomeId.hashCode());
    result = prime * result + ((orderField == null) ? 0 : orderField.hashCode());
    result = prime * result + ((page == null) ? 0 : page.hashCode());
    result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
    result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
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
    InteractionsResultFilteringOptionsData other = (InteractionsResultFilteringOptionsData) obj;
    if (interactomeId == null) {
      if (other.interactomeId != null)
        return false;
    } else if (!interactomeId.equals(other.interactomeId))
      return false;
    if (orderField != other.orderField)
      return false;
    if (page == null) {
      if (other.page != null)
        return false;
    } else if (!page.equals(other.page))
      return false;
    if (pageSize == null) {
      if (other.pageSize != null)
        return false;
    } else if (!pageSize.equals(other.pageSize))
      return false;
    if (sortDirection != other.sortDirection)
      return false;
    return true;
  }

}
