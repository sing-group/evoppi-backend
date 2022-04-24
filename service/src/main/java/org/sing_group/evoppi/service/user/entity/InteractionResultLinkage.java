/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.user.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class InteractionResultLinkage implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Set<String> linkedUuids;
  private final Set<String> linkageFailedUuids;

  public InteractionResultLinkage(Collection<String> linkedUuids, Collection<String> linkageFailedUuids) {
    this.linkedUuids = new HashSet<>(linkedUuids);
    this.linkageFailedUuids = new HashSet<>(linkageFailedUuids);
  }

  public Stream<String> getLinkedUuids() {
    return linkedUuids.stream();
  }

  public Stream<String> getLinkageFailedUuids() {
    return linkageFailedUuids.stream();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((linkageFailedUuids == null) ? 0 : linkageFailedUuids.hashCode());
    result = prime * result + ((linkedUuids == null) ? 0 : linkedUuids.hashCode());
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
    InteractionResultLinkage other = (InteractionResultLinkage) obj;
    if (linkageFailedUuids == null) {
      if (other.linkageFailedUuids != null)
        return false;
    } else if (!linkageFailedUuids.equals(other.linkageFailedUuids))
      return false;
    if (linkedUuids == null) {
      if (other.linkedUuids != null)
        return false;
    } else if (!linkedUuids.equals(other.linkedUuids))
      return false;
    return true;
  }

}
