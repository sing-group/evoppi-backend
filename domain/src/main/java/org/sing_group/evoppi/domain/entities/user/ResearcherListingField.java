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
package org.sing_group.evoppi.domain.entities.user;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum ResearcherListingField implements EntityListingField<Researcher> {
  LOGIN(true), EMAIL(true), ROLE(true);

  private final boolean supportsFiltering;

  private ResearcherListingField(boolean supportsFiltering) {
    this.supportsFiltering = supportsFiltering;
  }

  @Override
  public boolean isFilteringSupported() {
    return this.supportsFiltering;
  }

  @Override
  public <T, Q> Expression<T> getField(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Researcher> root) {
    switch (this) {
      case LOGIN:
        return root.get("login").get("login");
      case EMAIL:
        return root.get("credentials").get("email");
      case ROLE:
        return root.join("role");
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public <Q> Predicate getFilter(CriteriaBuilder cb, CriteriaQuery<Q> query, Root<Researcher> root, String value) {
    return cb.like(this.getField(cb, query, root), "%" + value + "%");
  }

}
