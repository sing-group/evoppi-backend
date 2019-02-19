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

package org.sing_group.evoppi.domain.entities.bio.query;

import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requirePositive;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GeneQueryOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private String prefix;
  private Set<Integer> interactomeIds;
  private int maxResults;

  GeneQueryOptions() {
    this("", emptySet(), 10);
  }

  public GeneQueryOptions(String prefix, int[] interactomeIds, int maxResults) {
    this(prefix, stream(interactomeIds).boxed().collect(Collectors.toSet()), maxResults);
  }
  
  public GeneQueryOptions(String prefix, Collection<Integer> interactomeIds, int maxResults) {
    this.setPrefix(prefix);
    this.setInteractomeIds(interactomeIds);
    this.setMaxResults(maxResults);
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = requireNonNull(prefix, "prefix can't be null");
  }

  public Set<Integer> getInteractomeIds() {
    return interactomeIds;
  }
  
  public boolean hasInteractomeIds() {
    return !this.interactomeIds.isEmpty();
  }

  public void setInteractomeIds(Collection<Integer> interactomeIds) {
    this.interactomeIds = interactomeIds == null ? emptySet() : new HashSet<>(interactomeIds);
  }

  public int getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(int maxResults) {
    this.maxResults = requirePositive(maxResults, "maxResults should be a positive value");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((interactomeIds == null) ? 0 : interactomeIds.hashCode());
    result = prime * result + maxResults;
    result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
    GeneQueryOptions other = (GeneQueryOptions) obj;
    if (interactomeIds == null) {
      if (other.interactomeIds != null)
        return false;
    } else if (!interactomeIds.equals(other.interactomeIds))
      return false;
    if (maxResults != other.maxResults)
      return false;
    if (prefix == null) {
      if (other.prefix != null)
        return false;
    } else if (!prefix.equals(other.prefix))
      return false;
    return true;
  }

}
