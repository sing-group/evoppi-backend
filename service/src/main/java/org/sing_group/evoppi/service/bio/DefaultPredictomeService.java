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
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.PredictomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.service.spi.bio.PredictomeService;

@Stateless
@PermitAll
public class DefaultPredictomeService implements PredictomeService {

  @Inject
  private PredictomeDAO dao;

//  @Inject
//  private InteractomeCreationWorkDAO workDao;
//
//  @Inject
//  private Event<InteractomeCreationRequestEvent> events;

  @Override
  public Stream<Predictome> list(ListingOptions<Predictome> listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public Predictome get(int id) {
    return this.dao.get(id);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<Predictome> listingOptions) {
    return this.dao.count(listingOptions);
  }

//  @Override
//  @RolesAllowed("ADMIN")
//  public InteractomeCreationWork createInteractome(InteractomeCreationData data) {
//    boolean exists = existsInteractomeWithName(data.getName());
//
//    if (exists) {
//      throw new IllegalArgumentException("Interactome name already exists");
//    } else {
//      final InteractomeCreationWork work = this.workDao.create(data.getName());
//
//      this.events.fire(new InteractomeCreationRequestEvent(data, work.getId()));
//
//      work.setScheduled();
//
//      return work;
//    }
//  }

//  private boolean existsInteractomeWithName(String name) {
//    try {
//      this.dao.getInteractomeByName(name);
//      return true;
//    } catch (IllegalArgumentException iae) {
//      return false;
//    }
//  }
}
