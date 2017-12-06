/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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


package org.sing_group.evoppi.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.domain.entities.user.Researcher;

public final class BaseRestPathBuilder implements RestPathBuilder {
  private final UriBuilder builder;
  
  public BaseRestPathBuilder(UriBuilder builder) {
    this.builder = builder;
  }
  
  public AdminRestPathBuilder admin() {
    return new AdminRestPathBuilder(this.builder);
  }
  
  public AdminRestPathBuilder admin(String login) {
    return new AdminRestPathBuilder(this.builder, login);
  }
  
  public AdminRestPathBuilder admin(Administrator admin) {
    return admin(admin.getLogin());
  }
  
  public ResearcherRestPathBuilder researcher() {
    return new ResearcherRestPathBuilder(builder);
  }
  
  public ResearcherRestPathBuilder researcher(String login) {
    return new ResearcherRestPathBuilder(builder, login);
  }
  
  public ResearcherRestPathBuilder researcher(Researcher researcher) {
    return researcher(researcher.getLogin());
  }
  
  public UserRoleRestPathBuilder userRole() {
    return new UserRoleRestPathBuilder(this.builder);
  }
  
  public URI build() {
    return this.builder.build();
  }
  
}
