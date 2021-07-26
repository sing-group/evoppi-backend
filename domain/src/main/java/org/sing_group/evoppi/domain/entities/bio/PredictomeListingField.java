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

public enum PredictomeListingField implements EntityListingField<Predictome> {
  NAME(true), SPECIESA(true), SPECIESB(true), CONVERSION_DATABASE(true), SOURCE_INTERACTOME(true);

  private final boolean supportsFiltering;

  private PredictomeListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @Override
  public <T, Q> Expression<T> getField(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Predictome> root) {
    switch (this) {
      case NAME:
        return root.get("name");
      case SOURCE_INTERACTOME:
        return root.get("sourceInteractome");
      case CONVERSION_DATABASE:
        return root.get("conversionDatabase");
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
    CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Predictome> root, String value
  ) {
    if (!this.isFilteringSupported()) {
      throw new UnsupportedOperationException();
    }

    switch (this) {
      case NAME:
      case SPECIESA:
      case SPECIESB:
      case SOURCE_INTERACTOME:
        return cb.like(this.getField(cb, query, root), "%" + value + "%");
      case CONVERSION_DATABASE:
        return cb.equal(this.getField(cb, query, root).as(String.class), value);
      default:
        throw new IllegalStateException();
    }

  }
}
