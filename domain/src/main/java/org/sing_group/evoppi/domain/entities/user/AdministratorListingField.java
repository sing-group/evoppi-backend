package org.sing_group.evoppi.domain.entities.user;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.EntityListingField;

public enum AdministratorListingField implements EntityListingField<Administrator> {
  LOGIN,
  EMAIL,
  ROLE;

  @Override
  public <F> Path<F> getField(Root<Administrator> root) {
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
  public Predicate getFilter(CriteriaBuilder cb, Root<Administrator> root, String value) {
    return cb.like(this.getField(root), "%" + value + "%");
  }

}
