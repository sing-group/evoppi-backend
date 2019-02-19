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

package org.sing_group.evoppi.service.security.check;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.ejb.Local;

import org.sing_group.evoppi.domain.entities.user.RoleType;

@Local
public interface SecurityCheckFactory {

  public SecurityCheck hasRole(RoleType role);

  public SecurityCheck hasAnyRoleOf(RoleType... roles);

  public SecurityCheck hasLogin(String login);
  
  public SecurityCheck hasLogin(Supplier<String> loginSupplier);
  
  public SecurityCheck hasAnyLoginOf(String ... login);
  
  public SecurityCheck hasAnyLoginOf(Supplier<String[]> loginSupplier);
  
  public SecurityCheck hasLoginAndRole(String login, RoleType role);
  
  public SecurityCheck hasLoginAndRole(Supplier<String> loginSupplier, RoleType role);
  
  public SecurityCheck hasLoginAndAnyRoleOf(String login, RoleType ... roles);
  
  public SecurityCheck hasLoginAndAnyRoleOf(Supplier<String> loginSupplier, RoleType ... roles);

  public SecurityCheck metsTheCondition(boolean condition);
  
  public SecurityCheck metsTheCondition(boolean condition, String description);

  public SecurityCheck metsTheCondition(BooleanSupplier condition);
  
  public SecurityCheck metsTheCondition(BooleanSupplier condition, String description);

}
