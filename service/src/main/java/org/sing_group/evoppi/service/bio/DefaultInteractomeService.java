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
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;

@Stateless
@PermitAll
public class DefaultInteractomeService implements InteractomeService {

  @Inject
  private InteractomeDAO dao;

  @Override
  public Interactome get(int id) {
    return this.dao.get(id);
  }
  
  @Override
  public Stream<Interactome> list(ListingOptions<Interactome> listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<Interactome> listingOptions) {
    return this.dao.count(listingOptions);
  }

  @RolesAllowed("ADMIN")
  public void remove(int id) {
    this.dao.delete(id);
  }
}
