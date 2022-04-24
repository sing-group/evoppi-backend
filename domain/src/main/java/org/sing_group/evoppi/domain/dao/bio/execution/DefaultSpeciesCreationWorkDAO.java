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
package org.sing_group.evoppi.domain.dao.bio.execution;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SpeciesCreationWorkDAO;
import org.sing_group.evoppi.domain.entities.execution.SpeciesCreationWork;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultSpeciesCreationWorkDAO implements SpeciesCreationWorkDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, SpeciesCreationWork> dh;

  public DefaultSpeciesCreationWorkDAO() {
    super();
  }

  public DefaultSpeciesCreationWorkDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, SpeciesCreationWork.class, this.em);
  }

  @Override
  public SpeciesCreationWork get(String uuid) {
    return this.dh.get(uuid)
      .orElseThrow(() -> new IllegalArgumentException("No result found with id: " + uuid));
  }

  @Override
  public void delete(String uuid) {
    this.dh.removeByKey(uuid);
  }

  @Override
  public SpeciesCreationWork create(String name) {
    return this.dh.persist(new SpeciesCreationWork(name));
  }
}
