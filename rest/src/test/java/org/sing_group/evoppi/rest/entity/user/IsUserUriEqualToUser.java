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

package org.sing_group.evoppi.rest.entity.user;

import java.net.URI;
import java.util.function.BiFunction;

import javax.ws.rs.core.UriBuilder;

import org.hamcrest.Factory;
import org.sing_group.evoppi.domain.entities.IsEqualToEntity;
import org.sing_group.evoppi.domain.entities.user.User;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

public class IsUserUriEqualToUser<T extends User> extends IsEqualToEntity<UserUri, T> {
  private final BiFunction<BaseRestPathBuilder, T, URI> uriBuilder;
  
  public IsUserUriEqualToUser(UserUri userUri, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder) {
    super(userUri);
    
    this.uriBuilder = uriBuilder;
  }

  @Override
  protected boolean matchesSafely(T actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      if (!this.expected.getLogin().equals(actual.getLogin())) {
        this.addDescription(String.format("actual login '%s' is different from expected login '%s'",
          actual.getLogin(), this.expected.getLogin()));
        
        return false;
      } else if (!this.checkUri(actual)) {
        this.addDescription(String.format("actual URI '%s' does not ends in '%s'",
          this.getActualUri(actual).getPath(), this.expected.getUri().getPath()));
        
        return false;
      } else {
        return true;
      }
    }
  }
  
  private URI getActualUri(T actual) {
    final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost");
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return this.uriBuilder.apply(pathBuilder, actual);
  }
  
  private boolean checkUri(T actual) {
    final URI actualUri = this.getActualUri(actual);
    final URI expectedUri = this.expected.getUri();
    
    return expectedUri.getPath().endsWith(actualUri.getPath());
  }
  
  @Factory
  public static <T extends User> IsUserUriEqualToUser<T> equalUserUriToUser(UserUri userUri, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder) {
    return new IsUserUriEqualToUser<T>(userUri, uriBuilder);
  }
}
