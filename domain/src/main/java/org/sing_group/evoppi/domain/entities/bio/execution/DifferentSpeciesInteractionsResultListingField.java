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
package org.sing_group.evoppi.domain.entities.bio.execution;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum DifferentSpeciesInteractionsResultListingField
  implements EntityListingField<DifferentSpeciesInteractionsResult> {
  SCHEDULING_DATE_TIME(false),
  QUERY_GENE(true),
  MAX_DEGREE(false),
  INTERACTOMES_COUNT(false),
  STATUS(true);

  private final boolean supportsFiltering;

  private DifferentSpeciesInteractionsResultListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T, Q> Expression<T> getField(
    CriteriaBuilder cb, CriteriaQuery<Q> query, Root<DifferentSpeciesInteractionsResult> root
  ) {
    switch (this) {
      case SCHEDULING_DATE_TIME:
        return root.get("status").get("schedulingDateTime");
      case QUERY_GENE:
        return root.join("queryGene").get("defaultName");
      case MAX_DEGREE:
        return root.get("queryMaxDegree");
      case INTERACTOMES_COUNT:
        return (Expression<T>) cb
          .sum(cb.size(root.get("targetInteractomes")), cb.size(root.get("referenceInteractomes")));
      case STATUS:
        return root.get("status").get("status");
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public <Q> Predicate getFilter(
    CriteriaBuilder cb, CriteriaQuery<Q> query, Root<DifferentSpeciesInteractionsResult> root, String value
  ) {
    if (!this.isFilteringSupported()) {
      throw new UnsupportedOperationException();
    }

    switch (this) {
      case QUERY_GENE:
        return cb.like(this.getField(cb, query, root), "%" + value + "%");
      case STATUS:
        return cb.equal(this.getField(cb, query, root).as(String.class), value);
      default:
        throw new IllegalStateException();
    }
  }

}
