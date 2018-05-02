/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.bio.entity;

import static org.sing_group.evoppi.domain.dao.SortDirection.DESCENDING;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.fluent.compare.Compare;

public class InteractionsResultFilter implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Integer start;
  private final Integer end;
  private final Comparator<InteractionGroupResult> sortStrategy;
  
  public InteractionsResultFilter() {
    this(null, null, null);
  }

  public InteractionsResultFilter(int start, int end) {
    this(start, end, null);
  }

  public InteractionsResultFilter(
    Comparator<InteractionGroupResult> sortStrategy
  ) {
    this(null, null, sortStrategy);
  }

  public InteractionsResultFilter(
    int start, int end, Comparator<InteractionGroupResult> sortStrategy
  ) {
    this(Integer.valueOf(start), Integer.valueOf(end), sortStrategy);
  }

  InteractionsResultFilter(
    Integer start, Integer end, Comparator<InteractionGroupResult> sortStrategy
  ) {
    if (start != null) {
      if (start < 0)
        throw new IllegalArgumentException("Start can't be negative");
      
      if (end != null && end < start)
        throw new IllegalArgumentException(String.format("End (%d) can't be lower than start (%d)", end, start));
    }
    
    this.start = start;
    this.end = end;
    this.sortStrategy = sortStrategy;
  }
  
  public static InteractionsResultFilterBuilder builder() {
    return new InteractionsResultFilterBuilder();
  }

  public Stream<InteractionGroupResult> filter(Stream<InteractionGroupResult> results) {
    if (this.sortStrategy != null)
      results = results.sorted(this.sortStrategy);
    
    if (this.start != null) {
      results = results.skip(this.start);
      
      if (this.end != null)
        results = results.limit(this.end - this.start + 1);
    } else {
      if (this.end != null)
        results = results.limit(this.end + 1);
    }
    
    return results;
  }

  public static class InteractionsResultFilterBuilder {
    private Integer start;
    private Integer end;
    private Comparator<InteractionGroupResult> sortStrategy;
    
    protected InteractionsResultFilterBuilder() {
      this.start = null;
      this.end = null;
      this.sortStrategy = null;
    }
    
    public InteractionsResultFilterBuilder between(int start, int end) {
      if (start < 0)
        throw new IllegalArgumentException("Start can't be negative");
      if (end < start)
        throw new IllegalArgumentException(String.format("End (%d) can't be lower than start (%d)", end, start));
      
      this.start = start;
      this.end = end;
      
      return this;
    }
    
    public InteractionsResultFilterBuilder paginated(int page, int pageSize) {
      if (page < 0)
        throw new IllegalArgumentException("Page can't be negative");
      if (pageSize <= 0)
        throw new IllegalArgumentException("Page must be positive");
      
      this.start = page * pageSize;
      this.end = this.start + pageSize - 1;
      
      return this;
    }

    public InteractionsResultFilterOrderBuilder sort(SortDirection sortDirection) {
      return new InteractionsResultFilterOrderBuilder(this, sortDirection);
    }
    
    protected InteractionsResultFilterBuilder setSortStrategy(Comparator<InteractionGroupResult> sortStrategy) {
      this.sortStrategy = sortStrategy;
      
      return this;
    }
    
    public InteractionsResultFilter build() {
      return new InteractionsResultFilter(start, end, sortStrategy);
    }
  }
  
  public static class InteractionsResultFilterOrderBuilder {
    private final InteractionsResultFilterBuilder builder;
    private final SortDirection sortDirection;

    protected InteractionsResultFilterOrderBuilder(
      InteractionsResultFilterBuilder builder,
      SortDirection sortDirection
    ) {
      this.builder = builder;
      this.sortDirection = sortDirection;
    }
    
    public InteractionsResultFilterBuilder byGeneAId() {
      Comparator<InteractionGroupResult> comparator =
        Compare.<InteractionGroupResult>createComparator()
          .byInt(InteractionGroupResult::getGeneAId)
          .thenByInt(InteractionGroupResult::getGeneBId)
        .andGetComparator();
      
      if (this.sortDirection == DESCENDING)
        comparator.reversed();
      
      return this.builder.setSortStrategy(comparator);
    }
    
    public InteractionsResultFilterBuilder byGeneBId() {
      Comparator<InteractionGroupResult> comparator =
        Compare.<InteractionGroupResult>createComparator()
          .byInt(InteractionGroupResult::getGeneBId)
          .thenByInt(InteractionGroupResult::getGeneAId)
        .andGetComparator();
      
      if (this.sortDirection == DESCENDING)
        comparator.reversed();
      
      return this.builder.setSortStrategy(comparator);
    }
    
    public InteractionsResultFilterBuilder byGeneAName(IntFunction<String> geneNameMapper) {
      Comparator<InteractionGroupResult> comparator =
        Compare.<InteractionGroupResult>createComparator()
          .by(result -> geneNameMapper.apply(result.getGeneAId()))
          .thenBy(result -> geneNameMapper.apply(result.getGeneBId()))
        .andGetComparator();
      
      if (this.sortDirection == DESCENDING)
        comparator.reversed();
      
      return this.builder.setSortStrategy(comparator);
    }
    
    public InteractionsResultFilterBuilder byGeneBName(IntFunction<String> geneNameMapper) {
      Comparator<InteractionGroupResult> comparator =
        Compare.<InteractionGroupResult>createComparator()
          .by(result -> geneNameMapper.apply(result.getGeneBId()))
          .thenBy(result -> geneNameMapper.apply(result.getGeneAId()))
        .andGetComparator();
      
      if (this.sortDirection == DESCENDING)
        comparator.reversed();
      
      return this.builder.setSortStrategy(comparator);
    }
    
    public InteractionsResultFilterBuilder byDegreeInInteractome(int interactomeId) {
      Comparator<InteractionGroupResult> comparator =
        Compare.<InteractionGroupResult>createComparator()
          .by(result -> result.getDegreeForInteractome(interactomeId))
        .andGetComparator();
      
      if (this.sortDirection == DESCENDING)
        comparator.reversed();
      
      return this.builder.setSortStrategy(comparator);
    }
  }
}
