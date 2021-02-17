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

package org.sing_group.evoppi.domain.dao;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ListingOptionsQueryBuilder<T> {
  private final ListingOptions<T> options;

  public ListingOptionsQueryBuilder(ListingOptions<T> options) {
    this.options = requireNonNull(options, "options can't be null");
  }

  public CriteriaQuery<T> addOrder(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root) {
    if (options.hasOrder()) {
      final List<Order> order =
        this.options.getSortFields()
          .map(sortField -> {
            switch (sortField.getDirection()) {
              case ASCENDING:
                return cb.asc(sortField.getField().getField(root));
              case DESCENDING:
                return cb.desc(sortField.getField().getField(root));
              default:
                throw new IllegalStateException("Invalid sort direction: " + sortField.getDirection());
            }
          })
          .collect(toList());

      return query.orderBy(order);
    } else {
      return query;
    }
  }
  
  public CriteriaQuery<T> addFilters(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root) {
    if (options.hasFilters()) {
      final Predicate[] filters = this.options.getFilterFields()
        .map(field -> field.getField().getFilter(cb, root, field.getValue()))
      .toArray(Predicate[]::new);
      
      final Predicate predicate = filters.length == 1 ? filters[0] : cb.and(filters);
      
      return query.where(predicate);
    } else {
      return query;
    }
  }

  public TypedQuery<T> addLimits(TypedQuery<T> query) {
    if (this.options.hasResultLimits()) {
      return query
        .setFirstResult(options.getStart().getAsInt())
        .setMaxResults(options.getMaxResults().getAsInt());
    } else {
      return query;
    }
  }
}
