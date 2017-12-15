/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static org.sing_group.fluent.checker.Checks.requireNonNegative;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGeneDAO implements GeneDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Gene> dh;

  public DefaultGeneDAO() {
    super();
  }

  public DefaultGeneDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Gene.class, this.em);
  }

  @Override
  public Stream<Gene> findByIdPrefix(int idPrefix, int maxResults) {
    requireNonNegative(idPrefix, "idPrefix can't be a negative number");
    
    final CriteriaBuilder cb = this.dh.cb();
    
    final CriteriaQuery<Gene> query = this.dh.createCBQuery();
    final Root<Gene> root = query.from(Gene.class);
    final Expression<String> fieldId = root.get("id").as(String.class);
    
    final Predicate predicate = cb.like(fieldId, idPrefix + "%");
    
    return em.createQuery(
      query.select(root)
        .where(predicate)
        .orderBy(cb.asc(fieldId))
    )
      .setMaxResults(maxResults)
      .getResultList()
    .stream();
  }

  @Override
  public Gene getGene(int geneId) {
    return this.dh.get(geneId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown gene: " + geneId));
  }
}