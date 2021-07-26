/*-
 * #%L
 * Service
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

package org.sing_group.evoppi.service.bio;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.DatabaseInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DatabaseInteractomeCreationWorkDAO;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.execution.DatabaseInteractomeCreationWork;
import org.sing_group.evoppi.service.bio.entity.DatabaseInteractomeCreationData;
import org.sing_group.evoppi.service.bio.interactome.event.DatabaseInteractomeCreationRequestEvent;
import org.sing_group.evoppi.service.spi.bio.DatabaseInteractomeService;

@Stateless
@PermitAll
public class DefaultDatabaseInteractomeService implements DatabaseInteractomeService {

  @Inject
  private InteractomeDAO interactomeDao;

  @Inject
  private DatabaseInteractomeDAO dao;

  @Inject
  private DatabaseInteractomeCreationWorkDAO workDao;

  @Inject
  private Event<DatabaseInteractomeCreationRequestEvent> events;

  @Override
  public Stream<DatabaseInteractome> list(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public DatabaseInteractome get(int id) {
    return this.dao.get(id);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dao.count(listingOptions);
  }

  @Override
  @RolesAllowed("ADMIN")
  public DatabaseInteractomeCreationWork create(DatabaseInteractomeCreationData data) {
    boolean exists = existsInteractomeWithName(data.getName());

    if (exists) {
      throw new IllegalArgumentException("Database interactome name already exists");
    } else {
      final DatabaseInteractomeCreationWork work = this.workDao.create(data.getName());

      this.events.fire(new DatabaseInteractomeCreationRequestEvent(data, work.getId()));

      work.setScheduled();

      return work;
    }
  }

  private boolean existsInteractomeWithName(String name) {
    try {
      this.interactomeDao.getByName(name);
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }
}
