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
import org.sing_group.evoppi.domain.dao.spi.bio.execution.InteractionsResultDAO;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractionsResultDAO implements InteractionsResultDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, InteractionsResult> dh;

  public DefaultInteractionsResultDAO() {
    super();
  }

  public DefaultInteractionsResultDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, InteractionsResult.class, this.em);
  }

  @Override
  public InteractionsResult get(int interactionResultId) {
    return this.dh.get(interactionResultId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interaction result: " + interactionResultId));
  }
  
  @Override
  public InteractionsResult createNew(int queryGeneId, int queryMaxDegree, int[] queryInteractomeIds) {
    return this.dh.persist(new InteractionsResult(queryGeneId, queryMaxDegree, queryInteractomeIds));
  }
}
