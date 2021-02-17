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

package org.sing_group.evoppi.domain.dao.bio;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.InteractomeListingOptions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractomeDAO implements InteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Interactome> dh;

  public DefaultInteractomeDAO() {
    super();
  }

  public DefaultInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Interactome.class, this.em);
  }

  @Override
  public Stream<Interactome> listInteractomes(InteractomeListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      CriteriaQuery<Interactome> query = dh.createCBQuery();
      final Root<Interactome> root = query.from(dh.getEntityType());

      query =
        query.select(root)
          .where(createPredicates(listingOptions, root))
          .orderBy(createOrders(listingOptions, root));

      TypedQuery<Interactome> typedQuery = em.createQuery(query);
      if (listingOptions.hasPagination()) {
        final int start = listingOptions.getStart().getAsInt();
        final int end = listingOptions.getEnd().getAsInt();

        typedQuery =
          typedQuery
            .setFirstResult(start)
            .setMaxResults(end - start + 1);
      }

      return typedQuery.getResultList().stream();
    }
  }

  private Predicate[] createPredicates(final InteractomeListingOptions listingOptions, final Root<Interactome> root) {
    final List<Predicate> andPredicates = new ArrayList<>();
    final CriteriaBuilder cb = dh.cb();

    String species = listingOptions.getSpecies();
    if (species != null && !species.isEmpty()) {
      andPredicates.add(cb.like(root.join("species").get("name"), "%" + species + "%"));
    }

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  private Order[] createOrders(final InteractomeListingOptions listingOptions, final Root<Interactome> root) {
    final List<Order> orders = new LinkedList<>();

    if (listingOptions.hasSortField()) {
      final CriteriaBuilder cb = dh.cb();
      final Function<Expression<?>, Order> order;

      switch (listingOptions.getSortDirection()) {
        case ASCENDING:
          order = cb::asc;
          break;
        case DESCENDING:
          order = cb::desc;
          break;
        default:
          order = null;
      }

      if (order != null) {
        switch (listingOptions.getSortField()) {
          case NAME:
            orders.add(order.apply(root.get("name")));
            break;
          case SPECIES:
            orders.add(order.apply(root.join("species").get("name")));
            break;
          case SOURCE_DB:
            orders.add(order.apply(root.get("dbSourceIdType")));
            break;

          case NONE:
            break;
          default:
            break;
        }
      }
    }

    return orders.toArray(new Order[orders.size()]);
  }

  @Override
  public Interactome getInteractome(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interactome: " + id));
  }

  @Override
  public long count() {
    return this.dh.count();
  }
}
