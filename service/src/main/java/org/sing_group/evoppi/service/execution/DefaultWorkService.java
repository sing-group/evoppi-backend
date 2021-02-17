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

package org.sing_group.evoppi.service.execution;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.execution.WorkDAO;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.domain.entities.user.RoleType;
import org.sing_group.evoppi.domain.entities.user.User;
import org.sing_group.evoppi.service.security.SecurityGuard;
import org.sing_group.evoppi.service.security.check.SecurityCheckFactory;
import org.sing_group.evoppi.service.spi.execution.WorkService;

@Stateless
@RolesAllowed("ADMIN")
public class DefaultWorkService implements WorkService {
  @Inject
  private WorkDAO dao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @Inject
  private SecurityCheckFactory checkThat;
  
  @PermitAll
  @Override
  public WorkEntity get(String id) {
    final WorkEntity work = this.dao.get(id);
    final Optional<User> owner = work.getOwner();
    
    return this.securityManager.ifAuthorized(
      checkThat.hasRole(RoleType.ADMIN),
      checkThat.metsTheCondition(!owner.isPresent(), "result does not have an owner"),
      checkThat.hasLogin(owner.map(User::getLogin).orElse(null))
    )
      .throwing(() -> new IllegalArgumentException("Unknown work id: " + id))
    .returnValue(work);
  }

  @Override
  public Stream<WorkEntity> list(ListingOptions<WorkEntity> options) {
    return this.dao.list(options);
  }
  
  @Override
  public long count() {
    return this.dao.count();
  }
}
