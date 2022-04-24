/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.resource.route;

import javax.ws.rs.core.UriBuilder;

public class UserRestPathBuilder {
  protected UriBuilder builder;
  
  public UserRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("user");
  }

  public RoleRestPathBuilder role() {
    return new RoleRestPathBuilder(this.builder);
  }
  
  public DifferentInteractionResult differentInteractionResult() {
    return new DifferentInteractionResult(this.builder);
  }
  
  public SameInteractionResult sameInteractionResult() {
    return new SameInteractionResult(this.builder);
  }
  
  public RegistrationPathBuilder registration() {
    return new RegistrationPathBuilder(builder);
  }
  
  public RegistrationPathBuilder registration(String uuid) {
    return new RegistrationPathBuilder(builder, uuid);
  }
}
