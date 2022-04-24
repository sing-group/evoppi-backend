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
package org.sing_group.evoppi.domain.dao;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;

public class ListingOptionsQueryBuilder<T> {
  public static <T, Q> CriteriaQuery<Q> addFilters(
    ListingOptions<T> options, CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root
  ) {
    if (options.hasFilters()) {
      return addFilters(options.getFilterFields(), cb, query, root);
    } else {
      return query;
    }
  }

  
  public static <T, Q> CriteriaQuery<Q> addFilters(
    Stream<FilterField<T>> filterFields, CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root
  ) {
    return buildFilters(filterFields, cb, query, root)
      .map(predicate -> query.where(predicate))
      .orElse(query);
  }
  
  public static <T, Q> Optional<Predicate> buildFilters(
    ListingOptions<T> options, CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root
  ) {
    if (options.hasFilters()) {
      return buildFilters(options.getFilterFields(), cb, query, root);
    } else {
      return Optional.empty();
    }
  }

  public static <T, Q> Optional<Predicate> buildFilters(
    Stream<FilterField<T>> filterFields, CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root
  ) {
    final Predicate[] filters = filterFields
      .map(field -> field.getField().getFilter(cb, query, root, field.getValue()))
    .toArray(Predicate[]::new);
    
    switch(filters.length) {
      case 0:
        return Optional.empty();
      case 1:
        return Optional.of(filters[0]);
      default:
        return Optional.of(cb.and(filters));
    }
  }
  
  private final ListingOptions<T> options;
  
  public ListingOptionsQueryBuilder(ListingOptions<T> options) {
    this.options = requireNonNull(options, "options can't be null");
  }

  public <Q> CriteriaQuery<Q> addOrder(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root) {
    if (options.hasOrder()) {
      final List<Order> order =
        this.options.getSortFields()
          .map(sortField -> {
            switch (sortField.getDirection()) {
              case ASCENDING:
                return cb.asc(sortField.getField().getField(cb, query, root));
              case DESCENDING:
                return cb.desc(sortField.getField().getField(cb, query, root));
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
  
  public <Q> CriteriaQuery<Q> addFilters(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root) {
    return this.buildFilters(cb, query, root)
      .map(predicate -> query.where(predicate))
      .orElse(query);
  }
  
  public <Q> Optional<Predicate> buildFilters(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<T> root) {
    return ListingOptionsQueryBuilder.buildFilters(this.options, cb, query, root);
  }

  public <Q> TypedQuery<Q> addLimits(TypedQuery<Q> query) {
    if (this.options.hasResultLimits()) {
      return query
        .setFirstResult(options.getStart().getAsInt())
        .setMaxResults(options.getMaxResults().getAsInt());
    } else {
      return query;
    }
  }
}
