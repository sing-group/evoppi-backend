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
package org.sing_group.evoppi.service.security.check;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Default;

import org.sing_group.evoppi.domain.entities.user.RoleType;

@Stateless
@Default
@PermitAll
@TransactionAttribute(SUPPORTS)
public class DefaultSecurityCheckFactory implements SecurityCheckFactory {
  @Resource
  private SessionContext context;
  
  private boolean isInAnyRoleOf(RoleType ... roles) {
    return stream(roles).map(RoleType::name).anyMatch(this.context::isCallerInRole);
  }
  
  private boolean isUserWithAnyLoginOf(String ... logins) {
    final String callerPrincipal = this.context.getCallerPrincipal().getName();
    
    return stream(logins).anyMatch(callerPrincipal::equals);
  }
  
  @Override
  public SecurityCheck metsTheCondition(boolean condition) {
    return this.metsTheCondition(condition, "condition is not met");
  }
  
  @Override
  public SecurityCheck metsTheCondition(boolean condition, String description) {
    return () -> condition ? SecurityCheckResult.valid() : SecurityCheckResult.invalid(description);
  }
  
  @Override
  public SecurityCheck metsTheCondition(BooleanSupplier condition) {
    return this.metsTheCondition(condition, "condition is not met");
  }
  
  @Override
  public SecurityCheck metsTheCondition(BooleanSupplier condition, String description) {
    return () -> condition.getAsBoolean() ? SecurityCheckResult.valid() : SecurityCheckResult.invalid(description);
  }

  @Override
  public SecurityCheck hasRole(RoleType role) {
    return () -> {
      if (isInAnyRoleOf(role)) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user is not in role: " + role);
      }
    };
  }

  @Override
  public SecurityCheck hasAnyRoleOf(RoleType ... roles) {
    return () -> {
      if (isInAnyRoleOf(roles)) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user is not in roles: " + Arrays.toString(roles));
      }
    };
  }
  
  @Override
  public SecurityCheck hasLogin(String login) {
    return this.hasLogin(() -> login);
  }
  
  @Override
  public SecurityCheck hasLogin(Supplier<String> loginSupplier) {
    return () -> {
      if (isUserWithAnyLoginOf(loginSupplier.get())) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user login is not the expected: " + loginSupplier.get());
      }
    };
  }

  @Override
  public SecurityCheck hasAnyLoginOf(String... logins) {
    return () -> {
      if (isUserWithAnyLoginOf(logins)) {
        return SecurityCheckResult.valid();
      } else {
        final String expectedLogins = stream(logins).collect(joining(", "));
        return SecurityCheckResult.invalid("user login is not the expected: " + expectedLogins);
      }
    };
  }

  @Override
  public SecurityCheck hasAnyLoginOf(Supplier<String[]> loginSuppliers) {
    return () -> {
      if (isUserWithAnyLoginOf(loginSuppliers.get())) {
        return SecurityCheckResult.valid();
      } else {
        final String expectedLogins = String.join(", ", loginSuppliers.get());
        return SecurityCheckResult.invalid("user login is not the expected: " + expectedLogins);
      }
    };
  }

  @Override
  public SecurityCheck hasLoginAndRole(String login, RoleType role) {
    return () -> {
      if (!isUserWithAnyLoginOf(login)) {
        return SecurityCheckResult.invalid("user login is not the expected: " + login);
      } else if (!isInAnyRoleOf(role)) {
        return SecurityCheckResult.invalid("user is not in role: " + role);
      } else {
        return SecurityCheckResult.valid();
      }
    };
  }

  @Override
  public SecurityCheck hasLoginAndRole(Supplier<String> loginSupplier, RoleType role) {
    return () -> {
      if (!isUserWithAnyLoginOf(loginSupplier.get())) {
        return SecurityCheckResult.invalid("user login is not the expected: " + loginSupplier.get());
      } else if (!isInAnyRoleOf(role)) {
        return SecurityCheckResult.invalid("user is not in role: " + role);
      } else {
        return SecurityCheckResult.valid();
      }
    };
  }

  @Override
  public SecurityCheck hasLoginAndAnyRoleOf(String login, RoleType... roles) {
    return () -> {
      if (!isUserWithAnyLoginOf(login)) {
        return SecurityCheckResult.invalid("user login is not the expected: " + login);
      } else if (!isInAnyRoleOf(roles)) {
        return SecurityCheckResult.invalid("user is not in roles: " + Arrays.toString(roles));
      } else {
        return SecurityCheckResult.valid();
      }
    };
  }

  @Override
  public SecurityCheck hasLoginAndAnyRoleOf(Supplier<String> loginSupplier, RoleType... roles) {
    return () -> {
      if (!isUserWithAnyLoginOf(loginSupplier.get())) {
        return SecurityCheckResult.invalid("user login is not the expected: " + loginSupplier.get());
      } else if (!isInAnyRoleOf(roles)) {
        return SecurityCheckResult.invalid("user is not in roles: " + Arrays.toString(roles));
      } else {
        return SecurityCheckResult.valid();
      }
    };
  }
}
