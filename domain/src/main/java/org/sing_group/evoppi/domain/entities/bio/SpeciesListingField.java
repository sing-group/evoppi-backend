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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum SpeciesListingField implements EntityListingField<Species> {
  NAME(true), INTERACTOMES_COUNT(false);

  private final boolean supportsFiltering;

  private SpeciesListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T, Q> Expression<T> getField(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Species> root) {
    switch (this) {
      case NAME:
        return root.get("name");
      case INTERACTOMES_COUNT:
        return (Expression<T>) cb.size(root.get("interactomes"));
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public <Q> Predicate getFilter(
    CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Species> root, String value
  ) {
    return cb.like(this.getField(cb, query, root), "%" + value + "%");
  }
}
