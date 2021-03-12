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
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.InteractomeCreationWorkDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.execution.InteractomeCreationWork;
import org.sing_group.evoppi.service.bio.interactome.event.InteractomeCreationRequestEvent;
import org.sing_group.evoppi.service.entity.bio.InteractomeCreationData;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;

@Stateless
@PermitAll
public class DefaultInteractomeService implements InteractomeService {

  @Inject
  private InteractomeDAO dao;

  @Inject
  private InteractomeCreationWorkDAO workDao;

  @Inject
  private Event<InteractomeCreationRequestEvent> events;

  @Override
  public Stream<Interactome> listInteractomes(ListingOptions<Interactome> interactomeListingOptions) {
    return this.dao.listInteractomes(interactomeListingOptions);
  }

  @Override
  public Interactome getInteractome(int id) {
    return this.dao.getInteractome(id);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<Interactome> interactomeListingOptions) {
    return this.dao.count(interactomeListingOptions);
  }

  @Override
  @RolesAllowed("ADMIN")
  public InteractomeCreationWork createInteractome(InteractomeCreationData data) {
    final InteractomeCreationWork work = this.workDao.create(data.getName());

    this.events
      .fire(new InteractomeCreationRequestEvent(data, work.getId()));

    work.setScheduled();

    return work;
  }
}
