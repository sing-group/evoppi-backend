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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.sing_group.fluent.checker.Checks.requireNonNegative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class ListingOptions<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  private final Integer start;
  private final Integer end;
  private final List<SortField<T>> sortFields;
  private final List<FilterField<T>> filterFields;

  public static <R> ListingOptions<R> noModification() {
    return new ListingOptions<>(null, null, null, null);
  }

  public static <R> ListingOptions<R> sortedBetween(
    Integer start, Integer end, EntityListingField<R> sortField, SortDirection sortDirection
  ) {
    if (sortField == null || sortDirection == null || sortDirection == SortDirection.NONE) {
      return new ListingOptions<R>(start, end, null, null);
    } else {
      return new ListingOptions<R>(start, end, singleton(new SortField<R>(sortField, sortDirection)), null);
    }
  }

  public static <R> ListingOptions<R> sortedAndFilteredBetween(
    Integer start, Integer end, EntityListingField<R> sortField, SortDirection sortDirection, List<FilterField<R>> filterFields
  ) {
    Collection<SortField<R>> sortFields;
    
    if (sortField == null || sortDirection == null || sortDirection == SortDirection.NONE) {
      sortFields = null;
    } else {
      sortFields = singleton(new SortField<R>(sortField, sortDirection));
    }
    
    return new ListingOptions<R>(start, end, sortFields, filterFields);
  }
  
  public static <R> ListingOptions<R> filtered(List<FilterField<R>> filterFields) {
    return new ListingOptions<R>(null, null, null, filterFields);
  }

  public ListingOptions(
    Integer start, Integer end, Collection<SortField<T>> sortFields, Collection<FilterField<T>> filterFields
  ) {
    if (start == null ^ end == null) {
      throw new IllegalArgumentException("start and end must be used together");
    } else if (start != null) {
      requireNonNegative(start, "start can't be negative");
      requireNonNegative(end, "end can't be negative");

      if (start > end)
        throw new IllegalArgumentException("start should be lower or equal to end");
    }

    this.start = start;
    this.end = end;

    this.sortFields = sortFields == null ? emptyList() : new ArrayList<>(sortFields);
    this.filterFields = filterFields == null ? emptyList() : new ArrayList<>(filterFields);
  }

  public boolean hasAnyQueryModifier() {
    return this.hasResultLimits() || this.hasOrder() || this.hasFilters();
  }

  public OptionalInt getStart() {
    return this.start == null ? OptionalInt.empty() : OptionalInt.of(start);
  }

  public OptionalInt getEnd() {
    return this.end == null ? OptionalInt.empty() : OptionalInt.of(end);
  }

  public boolean hasResultLimits() {
    return this.start != null;
  }

  public OptionalInt getMaxResults() {
    if (this.hasResultLimits()) {
      return OptionalInt.of(this.end - this.start + 1);
    } else {
      return OptionalInt.empty();
    }
  }

  public boolean hasOrder() {
    return !this.sortFields.isEmpty();
  }

  public Stream<SortField<T>> getSortFields() {
    return this.sortFields.stream();
  }

  public boolean hasFilters() {
    return !this.filterFields.isEmpty();
  }

  public Stream<FilterField<T>> getFilterFields() {
    return this.filterFields.stream();
  }

  @Override
  public int hashCode() {
    return Objects.hash(end, filterFields, sortFields, start);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ListingOptions<?> other = (ListingOptions<?>) obj;
    return Objects.equals(end, other.end) && Objects.equals(filterFields, other.filterFields)
      && Objects.equals(sortFields, other.sortFields) && Objects.equals(start, other.start);
  }

  public static class SortField<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final EntityListingField<T> field;
    private final SortDirection direction;

    public static <T> SortField<T> ascending(EntityListingField<T> field) {
      return new SortField<>(field, SortDirection.ASCENDING);
    }

    public static <T> SortField<T> descending(EntityListingField<T> field) {
      return new SortField<>(field, SortDirection.DESCENDING);
    }

    public SortField(EntityListingField<T> field, SortDirection direction) {
      this.field = field;
      this.direction = direction;
    }

    public EntityListingField<T> getField() {
      return field;
    }

    public SortDirection getDirection() {
      return direction;
    }

    @Override
    public int hashCode() {
      return Objects.hash(direction, field);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      SortField<?> other = (SortField<?>) obj;
      return direction == other.direction && Objects.equals(field, other.field);
    }
  }

  public static class FilterField<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static <T> Stream<FilterField<T>> buildFromUri(EntityListingField<T>[] fields, UriInfo uriInfo) {
      return buildFromUri(asList(fields), uriInfo);
    }
    
    public static <T> Stream<FilterField<T>> buildFromUri(Collection<EntityListingField<T>> fields, UriInfo uriInfo) {
      final MultivaluedMap<String, String> lowerCaseQueryParameters = new MultivaluedHashMap<>();

      uriInfo.getQueryParameters().forEach((k, v) -> {
        lowerCaseQueryParameters.put(k.toLowerCase(), v);
      });

      return fields.stream()
        .filter(EntityListingField::isFilteringSupported)
        .filter(field -> lowerCaseQueryParameters.containsKey(field.name().toLowerCase()))
        .map(field -> new FilterField<>(field, lowerCaseQueryParameters.getFirst(field.name().toLowerCase())));
    }

    private final EntityListingField<T> field;
    private final String value;

    public FilterField(EntityListingField<T> field, String value) {
      this.field = field;
      this.value = value;
    }

    public EntityListingField<T> getField() {
      return field;
    }

    public String getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(field, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      FilterField<?> other = (FilterField<?>) obj;
      return Objects.equals(field, other.field) && Objects.equals(value, other.value);
    }
  }
}
