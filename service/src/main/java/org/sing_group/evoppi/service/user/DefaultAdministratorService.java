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
package org.sing_group.evoppi.service.user;

import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.user.AdministratorDAO;
import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.service.spi.user.AdministratorService;

@Stateless
@RolesAllowed("ADMIN")
public class DefaultAdministratorService implements AdministratorService {
  @Inject
  private AdministratorDAO dao;

  @Override
  public Administrator get(String login) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    
    return dao.get(login);
  }

  @Override
  public Stream<Administrator> list(ListingOptions<Administrator> listingOptions) {
    return dao.list(listingOptions);
  }

  @Override
  public long count() {
    return this.dao.count(ListingOptions.noModification());
  }
  
  @Override
  public Administrator create(Administrator administrator) {
    return dao.create(administrator);
  }
  
  @Override
  public Administrator update(Administrator administrator) {
    return dao.update(administrator);
  }

  @Override
  public void delete(String login) {
    dao.delete(login);
  }
}
