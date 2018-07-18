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
package org.sing_group.evoppi.domain.dao.bio.execution;

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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.InteractionGroupResultDAO;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultInteractomeDegree;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultListingOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractionGroupResultDAO implements InteractionGroupResultDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, InteractionGroupResult> dh;

  public DefaultInteractionGroupResultDAO() {
    super();
  }

  public DefaultInteractionGroupResultDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, InteractionGroupResult.class, this.em);
  }
  
  @Override
  public Stream<InteractionGroupResult> getInteractions(
    InteractionsResult result, InteractionGroupResultListingOptions listingOptions
  ) {
    if (!listingOptions.hasAnyQueryModification()) {
      return result.getInteractions();
    } else {
      final CriteriaBuilder cb = dh.cb();
      
      CriteriaQuery<InteractionGroupResult> query = dh.createCBQuery();
      final Root<InteractionGroupResult> root = query.from(dh.getEntityType());
      
      final Path<InteractionsResult> fieldId = root.get("interactionsResult");
      
      query = query.select(root).where(cb.equal(fieldId, result));
      
      final Function<Expression<?>, Order> order;
      switch(listingOptions.getSortDirection()) {
        case ASCENDING:
          order = cb::asc;
          break;
        case DESCENDING:
          order = cb::desc;
          break;
        default:
          order = null;
      }
      if(order != null) {
        switch (listingOptions.getField()) {
          case GENE_A_ID:
            query = query.orderBy(
                    order.apply(root.get("geneA").get("id")),
                    order.apply(root.get("geneB").get("id"))
            );
            break;
          case GENE_B_ID:
            query = query.orderBy(
                    order.apply(root.get("geneB").get("id")),
                    order.apply(root.get("geneA").get("id"))
            );
            break;
          case GENE_A_NAME:
            query = query.orderBy(
                    order.apply(root.get("geneA").get("defaultName")),
                    order.apply(root.get("geneB").get("defaultName"))
            );
            break;
          case GENE_B_NAME:
            query = query.orderBy(
                    order.apply(root.get("geneB").get("defaultName")),
                    order.apply(root.get("geneA").get("defaultName"))
            );
            break;
          case INTERACTOME:
            Join<InteractionGroupResult, InteractionGroupResultInteractomeDegree> joinDegree =
                    root.join("interactomeDegrees", JoinType.LEFT);

            final Path<Integer> interactomeIdField = joinDegree.get("interactome").get("id");

            joinDegree = joinDegree.on(cb.equal(interactomeIdField, listingOptions.getInteractomeId().getAsInt()));

            final Path<Integer> degreeField = joinDegree.get("degree");

            query = query.orderBy(
                    order.apply(degreeField),
                    order.apply(root.get("geneA").get("id")),
                    order.apply(root.get("geneB").get("id"))
            );

            break;
          default:
        }
      }

      TypedQuery<InteractionGroupResult> typedQuery = em.createQuery(query);
      if (listingOptions.hasPagination()) {
        final int start = listingOptions.getStart().getAsInt();
        final int end = listingOptions.getEnd().getAsInt();
        
        typedQuery = typedQuery
          .setFirstResult(start)
          .setMaxResults(end - start + 1);
      }
      
      return typedQuery.getResultList().stream();
    }
  }
}
