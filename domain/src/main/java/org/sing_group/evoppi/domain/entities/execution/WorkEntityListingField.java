/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2021 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.domain.entities.execution;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum WorkEntityListingField implements EntityListingField<WorkEntity> {
  NAME(true),
  STATUS(true),
  CREATION_DATE_TIME(false),
  SCHEDULING_DATE_TIME(false),
  STARTING_DATE_TIME(false),
  FINISHING_DATE_TIME(false);

  private final boolean supportsFiltering;

  private WorkEntityListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @Override
  public <T, Q> Expression<T> getField(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<WorkEntity> root) {
    switch (this) {
      case NAME:
        return root.get("name");
      case STATUS:
        return root.join("status").get("status");
      case CREATION_DATE_TIME:
        return root.join("status").get("creationDateTime");
      case SCHEDULING_DATE_TIME:
        return root.join("status").get("schedulingDateTime");
      case STARTING_DATE_TIME:
        return root.join("status").get("startingDateTime");
      case FINISHING_DATE_TIME:
        return root.join("status").get("finishingDateTime");
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public <Q> Predicate getFilter(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<WorkEntity> root, String value) {
    if (this.isFilteringSupported()) {
      switch(this) {
        case STATUS:
          return cb.equal(this.getField(cb, query, root).as(String.class), value);
        default:
          return cb.like(this.getField(cb, query, root), "%" + value + "%");
      }
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
