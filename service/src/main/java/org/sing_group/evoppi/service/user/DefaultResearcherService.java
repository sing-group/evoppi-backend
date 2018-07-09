/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import org.sing_group.evoppi.domain.dao.spi.user.ResearcherDAO;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.domain.entities.user.RoleType;
import org.sing_group.evoppi.service.security.SecurityGuard;
import org.sing_group.evoppi.service.security.check.SecurityCheckBuilder;
import org.sing_group.evoppi.service.spi.user.ResearcherService;

@Stateless
@RolesAllowed("ADMIN")
public class DefaultResearcherService implements ResearcherService {
  @Inject
  private ResearcherDAO dao;
  
  @Inject
  private SecurityGuard securityGuard;
  
  @Inject
  private SecurityCheckBuilder checkThat;

  @RolesAllowed({ "ADMIN", "RESEARCHER" })
  @Override
  public Researcher get(String login) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    
    return this.securityGuard.ifAuthorized(
      checkThat.hasRole(RoleType.ADMIN),
      checkThat.hasLoginAndRole(login, RoleType.RESEARCHER)
    ).call(() -> dao.get(login));
  }

  @Override
  public Stream<Researcher> list(ListingOptions listingOptions) {
    return dao.list(listingOptions);
  }

  @Override
  public long count() {
    return this.dao.count();
  }
  
  @Override
  public Researcher create(Researcher researcher) {
    return dao.create(researcher);
  }
  
  @Override
  public Researcher update(Researcher researcher) {
    return dao.update(researcher);
  }

  @Override
  public void delete(String login) {
    dao.delete(login);
  }
}
