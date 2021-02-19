/*-
 * #%L
 * REST
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

package org.sing_group.evoppi.rest.resource.spi.user;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResultListingField;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResultListingField;
import org.sing_group.evoppi.rest.entity.bio.ResultUuids;
import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;

@Local
public interface UserResource {

  public Response role(String login, String password);
  
  public Response register(UserRegistrationData registration);

  public Response confirm(String code);

  public Response claimDifferentSpeciesResults(ResultUuids uuids);
  
  public Response claimSameSpeciesResults(ResultUuids uuids);
  
  public Response changePassword(String password);
  
  public Response requestPasswordRecovery(String login);
  
  public Response recoverPassword(String code, String newPassword);

  Response listDifferentSpeciesResults(
    Integer start, Integer end, DifferentSpeciesInteractionsResultListingField order, SortDirection sort
  );

  Response listSameSpeciesResults(
    Integer start, Integer end, SameSpeciesInteractionsResultListingField order, SortDirection sort
  );

}
