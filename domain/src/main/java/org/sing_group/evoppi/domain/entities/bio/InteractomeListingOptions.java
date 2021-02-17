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

import java.util.Objects;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.SortDirection;

public class InteractomeListingOptions extends ListingOptions {
  private static final long serialVersionUID = 1L;

  private final String species;

  private static SortField[] buildSortField(
    InteractomeListingField orderField, SortDirection sortDirection
  ) {

    if (orderField == null ^ sortDirection == null) {
      throw new IllegalArgumentException("orderField and sortDirection must be used together");
    }

    if (orderField == null) {
      return new SortField[0];
    } else {
      return new SortField[] {
        new SortField(orderField.name(), sortDirection)
      };
    }
  }

  public InteractomeListingOptions(
    Integer start, Integer end,
    InteractomeListingField orderField,
    SortDirection sortDirection,
    String species
  ) {
    super(start, end, buildSortField(orderField, sortDirection));

    this.species = species;
  }

  public boolean hasAnyQueryModification() {
    return this.hasResultLimits() || this.hasOrder() || this.hasFilters();
  }

  public boolean hasFilters() {
    return this.species != null && !this.species.isEmpty();
  }

  public InteractomeListingField getSortField() {
    return this.getSortFields()
      .findFirst()
      .map(field -> InteractomeListingField.valueOf(field.getSortField()))
      .orElse(null);
  }

  public SortDirection getSortDirection() {
    return this.getSortFields()
      .findFirst()
      .map(SortField::getSortDirection)
      .orElse(null);
  }

  public String getSpecies() {
    return species;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(species);
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
    InteractomeListingOptions other = (InteractomeListingOptions) obj;
    return Objects.equals(species, other.species);
  }
}
