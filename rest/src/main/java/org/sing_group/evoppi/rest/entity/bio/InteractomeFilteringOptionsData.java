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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.InteractomeListingField;
import org.sing_group.evoppi.domain.entities.bio.InteractomeListingOptions;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactome-filtering-options", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactome-filtering-options", description = "Filtering options for interactomes.")
public class InteractomeFilteringOptionsData {
  private Integer page;
  private Integer pageSize;
  private InteractomeListingField orderField;
  private SortDirection sortDirection;
  private String species;

  InteractomeFilteringOptionsData() {}

  public InteractomeFilteringOptionsData(
    Integer page, Integer pageSize,
    InteractomeListingField orderField, SortDirection sortDirection,
    String species
  ) {
    if (page == null ^ pageSize == null) {
      throw new IllegalArgumentException("page and pageSize must be used together");
    }

    if (orderField == null ^ sortDirection == null) {
      throw new IllegalArgumentException("orderField and sortDirection must be used together");
    }

    this.page = page;
    this.pageSize = pageSize;
    this.orderField = orderField;
    this.sortDirection = sortDirection;
    this.species = species;
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

  public InteractomeListingField getOrderField() {
    return orderField;
  }

  public void setOrderField(InteractomeListingField orderField) {
    this.orderField = orderField;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public boolean hasPagination() {
    return this.page != null && this.pageSize != null;
  }

  public boolean hasOrder() {
    return this.orderField != null;
  }
  
  public String getSpecies() {
    return species;
  }
  
  public void setSpecies(String species) {
    this.species = species;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((orderField == null) ? 0 : orderField.hashCode());
    result = prime * result + ((page == null) ? 0 : page.hashCode());
    result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
    result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
    result = prime * result + ((species == null) ? 0 : species.hashCode());
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
    InteractomeFilteringOptionsData other = (InteractomeFilteringOptionsData) obj;
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
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }

  public InteractomeListingOptions toInteractomeListingOptions() {
    final Integer start, end;

    if (this.hasPagination()) {
      start = this.getPage() * this.getPageSize();
      end = start + this.getPageSize() - 1;
    } else {
      start = end = null;
    }

    return new InteractomeListingOptions(
      start, end,
      this.getOrderField(),
      this.getSortDirection(),
      this.species
    );
  }
}
