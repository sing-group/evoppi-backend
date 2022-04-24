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
package org.sing_group.evoppi.domain.dao.info;

import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.info.DatabaseInformationDao;
import org.sing_group.evoppi.domain.entities.DatabaseInformation;

@Default
@Transactional(MANDATORY)
public class DefaultDatabaseInformationDao implements DatabaseInformationDao {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, DatabaseInformation> dh;

  DefaultDatabaseInformationDao() {}

  public DefaultDatabaseInformationDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, DatabaseInformation.class, this.em);
  }

  @Override
  public Optional<DatabaseInformation> get() {
    return this.dh.list().stream().findFirst();
  }
}
