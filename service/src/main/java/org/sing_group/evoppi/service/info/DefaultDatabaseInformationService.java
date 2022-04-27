/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.info;

import javax.annotation.security.PermitAll;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.info.DatabaseInformationDao;
import org.sing_group.evoppi.service.spi.info.DatabaseInformationService;

@Startup
@Singleton
@PermitAll
public class DefaultDatabaseInformationService implements DatabaseInformationService {

  @Inject
  private DatabaseInformationDao databaseInformationDao;

  @Override
  public String getDbVersion() {
    return this.databaseInformationDao.get().get().getVersion();
  }

  @Override
  public long getSpeciesCount() {
    return this.databaseInformationDao.get().get().getSpeciesCount();
  }

  @Override
  public long getDatabaseInteractomesCount() {
    return this.databaseInformationDao.get().get().getDatabaseInteractomesCount();
  }

  @Override
  public long getPredictomesCount() {
    return this.databaseInformationDao.get().get().getPredictomesCount();
  }

  @Override
  public long getGenesCount() {
    return this.databaseInformationDao.get().get().getGenesCount();
  }

  @Override
  public long getInteractionsCount() {
    return this.databaseInformationDao.get().get().getInteractionsCount();
  }
}
