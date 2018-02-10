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
package org.sing_group.evoppi.domain.dao.bio.execution;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DifferentSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultDifferentSpeciesInteractionsResultDAO implements DifferentSpeciesInteractionsResultDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, DifferentSpeciesInteractionsResult> dh;

  public DefaultDifferentSpeciesInteractionsResultDAO() {
    super();
  }

  public DefaultDifferentSpeciesInteractionsResultDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, DifferentSpeciesInteractionsResult.class, this.em);
  }
  
  @Override
  public boolean exists(String interactionResultId) {
    try {
      this.get(interactionResultId);
      
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }

  @Override
  public DifferentSpeciesInteractionsResult get(String interactionResultId) {
    return this.dh.get(interactionResultId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interaction result: " + interactionResultId));
  }

  @Override
  public DifferentSpeciesInteractionsResult create(
    int queryGeneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastOptions, int queryMaxDegree
  ) {
    return this.dh.persist(
      new DifferentSpeciesInteractionsResult(
        queryGeneId, referenceInteractome, targetInteractome, blastOptions, queryMaxDegree
      )
    );
  }
}
