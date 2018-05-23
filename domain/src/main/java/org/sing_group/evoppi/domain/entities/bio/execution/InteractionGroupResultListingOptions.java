/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.bio.execution;

import static org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultField.INTERACTOME;

import java.io.Serializable;
import java.util.OptionalInt;

import org.sing_group.evoppi.domain.dao.SortDirection;

public class InteractionGroupResultListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Integer start;
  private final Integer end;
  private final InteractionGroupResultField field;
  private final SortDirection sortDirection;
  
  private final Integer interactomeId;

  public InteractionGroupResultListingOptions(
    Integer start, Integer end,
    InteractionGroupResultField orderField,
    SortDirection sortDirection,
    Integer interactomeId
  ) {
    if (start == null ^ end == null) {
      throw new IllegalArgumentException("start and end must be used together");
    }
    
    if (orderField == null ^ sortDirection == null) {
      throw new IllegalArgumentException("orderField and sortDirection must be used together");
    } else {
      if (orderField == INTERACTOME ^ interactomeId != null) {
        throw new IllegalArgumentException("interactomeId must be non null when orderField is INTERACTOME");
      }
    }
    
    this.start = start;
    this.end = end;
    this.field = orderField;
    this.sortDirection = sortDirection;
    this.interactomeId = interactomeId;
  }
  
  public boolean hasAnyQueryModification() {
    return this.hasPagination() || this.hasSortField();
  }
  
  public boolean hasPagination() {
    return this.start != null && this.end != null;
  }

  public OptionalInt getStart() {
    return start == null ? OptionalInt.empty() : OptionalInt.of(start);
  }

  public OptionalInt getEnd() {
    return end == null ? OptionalInt.empty() : OptionalInt.of(end);
  }

  public boolean hasSortField() {
    return this.field != InteractionGroupResultField.NONE;
  }
  
  public InteractionGroupResultField getField() {
    return field;
  }
  
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public OptionalInt getInteractomeId() {
    return interactomeId == null ? OptionalInt.empty() : OptionalInt.of(interactomeId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((field == null) ? 0 : field.hashCode());
    result = prime * result + ((interactomeId == null) ? 0 : interactomeId.hashCode());
    result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InteractionGroupResultListingOptions other = (InteractionGroupResultListingOptions) obj;
    if (end == null) {
      if (other.end != null)
        return false;
    } else if (!end.equals(other.end))
      return false;
    if (field != other.field)
      return false;
    if (interactomeId == null) {
      if (other.interactomeId != null)
        return false;
    } else if (!interactomeId.equals(other.interactomeId))
      return false;
    if (sortDirection != other.sortDirection)
      return false;
    if (start == null) {
      if (other.start != null)
        return false;
    } else if (!start.equals(other.start))
      return false;
    return true;
  }
}
