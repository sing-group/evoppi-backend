/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;
import java.util.OptionalInt;

import org.sing_group.evoppi.domain.dao.SortDirection;

public class InteractomeListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final Integer start;
  private final Integer end;
  private final InteractomeListingField sortField;
  private final SortDirection sortDirection;
  private final String species;

  public InteractomeListingOptions(
    Integer start, Integer end,
    InteractomeListingField orderField,
    SortDirection sortDirection,
    String species
  ) {
    if (start == null ^ end == null) {
      throw new IllegalArgumentException("start and end must be used together");
    }

    if (orderField == null ^ sortDirection == null) {
      throw new IllegalArgumentException("orderField and sortDirection must be used together");
    }

    this.start = start;
    this.end = end;
    this.sortField = orderField;
    this.sortDirection = sortDirection;
    this.species = species;
  }

  public boolean hasAnyQueryModification() {
    return this.hasPagination() || this.hasSortField() || this.hasFilters();
  }

  public boolean hasPagination() {
    return this.start != null && this.end != null;
  }

  public OptionalInt getStart() {
    return start == null ? OptionalInt.empty() : OptionalInt.of(start);
  }

  public OptionalInt getEnd() {
    return end == null ? OptionalInt.empty() : OptionalInt.of(end);
  }

  public boolean hasSortField() {
    return this.sortField != InteractomeListingField.NONE;
  }

  public boolean hasFilters() {
    return this.species != null && !this.species.isEmpty();
  }

  public InteractomeListingField getSortField() {
    return sortField;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public String getSpecies() {
    return species;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((sortField == null) ? 0 : sortField.hashCode());
    result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
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
    InteractomeListingOptions other = (InteractomeListingOptions) obj;
    if (end == null) {
      if (other.end != null)
        return false;
    } else if (!end.equals(other.end))
      return false;
    if (sortField != other.sortField)
      return false;
    if (sortDirection != other.sortDirection)
      return false;
    if (start == null) {
      if (other.start != null)
        return false;
    } else if (!start.equals(other.start))
      return false;
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }
}
