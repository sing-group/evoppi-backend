package org.sing_group.evoppi.domain.entities.execution;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum WorkEntityListingField implements EntityListingField<WorkEntity> {
  NAME,
  STATUS,
  CREATION_DATE_TIME,
  SCHEDULING_DATE_TIME,
  STARTING_DATE_TIME,
  FINISHING_DATE_TIME;

  @Override
  public <F> Path<F> getField(Root<WorkEntity> root) {
    switch (this) {
      case NAME:
        return root.get("name");
      case STATUS:
        return root.join("status").get("name");
      case CREATION_DATE_TIME:
        return root.join("status").get("creationDateTime");
      case SCHEDULING_DATE_TIME:
        return root.join("status").get("schedulingDateTime");
      case STARTING_DATE_TIME:
        return root.join("status").get("startingDateTime");
      case FINISHING_DATE_TIME:
        return root.join("status").get("finishingDateTime");
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public Predicate getFilter(CriteriaBuilder cb, Root<WorkEntity> root, String value) {
    switch (this) {
      case NAME:
      case STATUS:
        return cb.like(this.getField(root), "%" + value + "%");
      case CREATION_DATE_TIME:
      case SCHEDULING_DATE_TIME:
      case STARTING_DATE_TIME:
      case FINISHING_DATE_TIME:
        throw new UnsupportedOperationException();
      default:
        throw new IllegalStateException();
    }
  }
}
