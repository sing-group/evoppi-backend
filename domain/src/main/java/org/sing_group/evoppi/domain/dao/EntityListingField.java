package org.sing_group.evoppi.domain.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface EntityListingField<R> {
  public <F> Path<F> getField(Root<R> root);
  public Predicate getFilter(CriteriaBuilder cb, Root<R> root, String value);
}
