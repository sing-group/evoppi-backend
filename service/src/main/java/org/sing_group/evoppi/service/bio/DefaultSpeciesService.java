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
package org.sing_group.evoppi.service.bio;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SpeciesCreationWorkDAO;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.execution.SpeciesCreationWork;
import org.sing_group.evoppi.service.bio.entity.SpeciesCreationData;
import org.sing_group.evoppi.service.bio.species.event.SpeciesCreationRequestEvent;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;

@Stateless
@PermitAll
public class DefaultSpeciesService implements SpeciesService {

  @Inject
  private SpeciesDAO dao;

  @Inject
  private SpeciesCreationWorkDAO workDao;

  @Inject
  private Event<SpeciesCreationRequestEvent> events;

  @Override
  public Stream<Species> listSpecies(ListingOptions<Species> speciesListingOptions) {
    return this.dao.listSpecies(speciesListingOptions);
  }

  @Override
  public Species getSpecies(int id) {
    return this.dao.getSpecies(id);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<Species> speciesListingOptions) {
    return this.dao.count(speciesListingOptions);
  }

  @Override
  @RolesAllowed("ADMIN")
  public SpeciesCreationWork createSpecies(SpeciesCreationData data) {
    boolean exists = existsSpeciesWithName(data.getName());

    if (exists) {
      throw new IllegalArgumentException("Species name already exists");
    } else {
      final SpeciesCreationWork work = this.workDao.create(data.getName());

      this.events.fire(new SpeciesCreationRequestEvent(data, work.getId()));

      work.setScheduled();

      return work;
    }
  }

  private boolean existsSpeciesWithName(String name) {
    try {
      this.dao.getSpeciesByName(name);
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }

  @Override
  @RolesAllowed("ADMIN")
  public void removeSpecies(int id) {
    this.dao.deleteSpecies(id);
  }
}
