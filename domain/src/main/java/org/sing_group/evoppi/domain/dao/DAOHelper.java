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

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;
import static org.sing_group.fluent.checker.Checks.requireNonNullCollection;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;

public class DAOHelper<K, T> {
  private final EntityManager em;
  private final Class<T> entityClass;

  public static <K, T> DAOHelper<K, T> of(Class<K> keyClass, Class<T> entityClass, EntityManager em) {
    return new DAOHelper<>(keyClass, entityClass, em);
  }

  private DAOHelper(Class<K> keyClass, Class<T> entityClass, EntityManager em) {
    this.entityClass = entityClass;
    this.em = em;
  }

  public Class<T> getEntityType() {
    return this.entityClass;
  }

  public Optional<T> get(K key) {
    return Optional.ofNullable(this.em.find(this.getEntityType(), requireNonNull(key)));
  }

  public T persist(T entity) {
    try {
      this.em.persist(entity);
      this.em.flush();

      return entity;
    } catch (PersistenceException pe) {
      throw new IllegalArgumentException("Entity already exists", pe);
    }
  }

  public T update(T entity) {
    final T mergedEntity = this.em.merge(entity);
    this.em.flush();

    return mergedEntity;
  }

  public List<T> list() {
    final CriteriaQuery<T> query = createCBQuery();

    return em.createQuery(
      query.select(query.from(getEntityType()))
    ).getResultList();
  }

  public List<T> list(ListingOptions<T> options) {
    if (!options.hasAnyQueryModifier()) {
      return this.list();
    } else {
      final ListingOptionsQueryBuilder<T> optionsBuilder = new ListingOptionsQueryBuilder<>(options);

      final CriteriaQuery<T> queryBuilder = createCBQuery();
      final Root<T> root = queryBuilder.from(getEntityType());

      CriteriaQuery<T> select = optionsBuilder.addOrder(cb(), queryBuilder.select(root), root);

      select = optionsBuilder.addFilters(cb(), select, root);

      final TypedQuery<T> query = optionsBuilder.addLimits(em.createQuery(select));

      return query.getResultList();
    }
  }

  public List<T> list(ListingOptions<T> options, BiFunction<CriteriaBuilder, Root<T>, Predicate[]> predicatesBuilder) {
    final ListingOptionsQueryBuilder<T> optionsBuilder = new ListingOptionsQueryBuilder<>(options);

    final CriteriaQuery<T> queryBuilder = createCBQuery();
    final Root<T> root = queryBuilder.from(getEntityType());

    CriteriaQuery<T> select = optionsBuilder.addOrder(cb(), queryBuilder.select(root), root);

    final Predicate[] predicates = predicatesBuilder.apply(cb(), root);

    if (predicates.length > 0)
      select = select.where(predicates);

    final TypedQuery<T> query = optionsBuilder.addLimits(em.createQuery(select));

    return query.getResultList();
  }

  public void removeByKey(K key) {
    this.em.remove(get(key).orElseThrow(() -> new IllegalArgumentException("No entity found with id: " + key)));
    this.em.flush();
  }

  public void remove(T entity) {
    this.em.remove(entity);
    this.em.flush();
  }

  public <F> void deleteBy(String field, F value) {
    this.deleteBy((query, root) -> query.where(cb().equal(root.get(field), value)));
  }

  public <F> void deleteBy(String field, Collection<F> values) {
    this.deleteBy((query, root) -> query.where(root.get(field).in(values)));
  }
  
  private <F> void deleteBy(BiConsumer<CriteriaDelete<T>, Root<T>> whereBuilder) {
    final CriteriaBuilder cb = this.cb();
    
    final CriteriaDelete<T> query = cb.createCriteriaDelete(this.entityClass);
    final Root<T> root = query.from(this.entityClass);

    whereBuilder.accept(query, root);

    this.em.createQuery(query).executeUpdate();
    this.em.flush();
    
  }

  public final <F> List<T> listBy(String fieldName, F values) {
    return this.listBy(fieldName, singleton(values), ListingOptions.noModification());
  }

  public final <F> List<T> listBy(String fieldName, F values, ListingOptions<T> listingOptions) {
    return createFieldQuery(fieldName, singleton(values), listingOptions)
      .getResultList();
  }

  public final <F> List<T> listBy(String fieldName, Set<F> values) {
    return this.listBy(fieldName, values, ListingOptions.noModification());
  }

  public final <F> List<T> listBy(String fieldName, Set<F> values, ListingOptions<T> listingOptions) {
    return createFieldQuery(fieldName, values, listingOptions)
      .getResultList();
  }

  public <F> T getBy(String fieldName, F value) {
    return getBy(fieldName, value, ListingOptions.noModification());
  }

  public <F> T getBy(String fieldName, F value, ListingOptions<T> listingOptions) {
    return createFieldQuery(fieldName, singleton(value), listingOptions)
      .getSingleResult();
  }

  public long count() {
    CriteriaQuery<Long> query = cb().createQuery(Long.class);

    query = query.select(cb().count(query.from(this.getEntityType())));

    return this.em.createQuery(query).getSingleResult();
  }

  public <F> long countBy(String fieldName, F value) {
    return this.countBy(fieldName, singleton(value));
  }

  public <F> long countBy(String fieldName, Set<F> values) {
    CriteriaQuery<Long> query = cb().createQuery(Long.class);
    final Root<T> root = query.from(getEntityType());

    final Predicate predicate = this.createFieldEqualityPredicate(fieldName, values, root);

    query = query.select(cb().count(root)).where(predicate);

    return this.em.createQuery(query).getSingleResult();
  }

  public long count(ListingOptions<T> listingOptions) {
    return this.count(listingOptions.getFilterFields());
  }

  public <F> long countBy(String fieldName, F value, ListingOptions<T> listingOptions) {
    return this.countBy(fieldName, singleton(value), listingOptions.getFilterFields());
  }

  public <F> long countBy(String fieldName, Set<F> values, ListingOptions<T> listingOptions) {
    return this.countBy(fieldName, values, listingOptions.getFilterFields());
  }

  public long count(Stream<FilterField<T>> filters) {
    CriteriaQuery<Long> query = cb().createQuery(Long.class);

    final Root<T> root = query.from(this.getEntityType());
    query = query.select(cb().count(root));
    
    query = ListingOptionsQueryBuilder.addFilters(filters, cb(), query, root);

    return this.em.createQuery(query).getSingleResult();
  }

  public <F> long countBy(String fieldName, F value, Stream<FilterField<T>> filters) {
    return this.countBy(fieldName, singleton(value), filters);
  }

  public <F> long countBy(String fieldName, Set<F> values, Stream<FilterField<T>> filters) {
    CriteriaQuery<Long> query = cb().createQuery(Long.class);
    final Root<T> root = query.from(getEntityType());

    Predicate predicate = this.createFieldEqualityPredicate(fieldName, values, root);
    
    final Optional<Predicate> filterPredicate = ListingOptionsQueryBuilder.buildFilters(filters, cb(), query, root);

    if (filterPredicate.isPresent()) {
      predicate = cb().and(predicate, filterPredicate.get());
    }
    query = query.select(cb().count(root)).where(predicate);
    
    return this.em.createQuery(query).getSingleResult();
  }

  public final <F> TypedQuery<T> createFieldQuery(
    String fieldName,
    Set<F> values,
    ListingOptions<T> listingOptions
  ) {
    requireNonNull(fieldName, "Fieldname can't be null");
    requireNonNullCollection(values);
    requireNonEmpty(values, "At least one value must be provided for the field");

    CriteriaQuery<T> query = createCBQuery();
    final Root<T> root = query.from(getEntityType());

    final ListingOptionsQueryBuilder<T> queryBuilder = new ListingOptionsQueryBuilder<>(listingOptions);

    final Predicate predicate = this.createFieldEqualityPredicate(fieldName, values, root);

    final Optional<Predicate> queryFilters = queryBuilder.buildFilters(cb(), query, root);
    if (queryFilters.isPresent()) {
      query = query.select(root).where(cb().and(predicate, queryFilters.get()));
    } else {
      query = query.select(root).where(predicate);
    }

    query = queryBuilder.addOrder(cb(), query, root);

    return queryBuilder.addLimits(em.createQuery(query));
  }

  private <F> Predicate createFieldEqualityPredicate(String fieldName, Set<F> values, final Root<T> root) {
    final Function<F, Predicate> fieldEqualsTo = value -> cb().equal(getField(root, fieldName), value);

    return values.size() == 1
      ? fieldEqualsTo.apply(values.iterator().next())
      : cb().or(
        values.stream()
          .map(fieldEqualsTo)
          .toArray(Predicate[]::new)
      );
  }

  private Path<T> getField(Root<T> root, String fieldName) {
    if (fieldName.contains(".")) {
      final String[] fieldParts = fieldName.split("[.]");

      Path<T> field = root.get(fieldParts[0]);

      for (int i = 1; i < fieldParts.length; i++) {
        field = field.get(fieldParts[i]);
      }

      return field;

    } else {
      return root.get(fieldName);
    }
  }

  public EntityManager em() {
    return this.em;
  }

  public CriteriaQuery<T> createCBQuery() {
    return cb().createQuery(this.getEntityType());
  }

  public CriteriaBuilder cb() {
    return em.getCriteriaBuilder();
  }
}
