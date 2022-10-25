/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.bio;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum DatabaseInteractomeListingField implements EntityListingField<DatabaseInteractome> {
  NAME(true), SOURCE_DB(true), SPECIESA(true), SPECIESB(true), COLLECTION(true);

  private final boolean supportsFiltering;

  private DatabaseInteractomeListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @Override
  public <T, Q> Expression<T> getField(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<DatabaseInteractome> root) {
    switch (this) {
      case NAME:
        return root.get("name");
      case COLLECTION:
        return root.join("collection").get("name");
      case SOURCE_DB:
        return root.get("dbSourceIdType");
      case SPECIESA:
        return root.join("speciesA").get("name");
      case SPECIESB:
        return root.join("speciesB").get("name");
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public <Q> Predicate getFilter(
    CriteriaBuilder cb, CriteriaQuery<Q> query, Root<DatabaseInteractome> root, String value
  ) {
    if (!this.isFilteringSupported()) {
      throw new UnsupportedOperationException();
    }

    switch (this) {
      case NAME:
      case SOURCE_DB:
      case SPECIESA:
      case SPECIESB:
      case COLLECTION:
        return cb.like(this.getField(cb, query, root).as(String.class), "%" + value + "%");
      default:
        throw new IllegalStateException();
    }
  }
}
