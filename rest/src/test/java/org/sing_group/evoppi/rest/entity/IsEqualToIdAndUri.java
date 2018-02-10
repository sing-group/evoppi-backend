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


package org.sing_group.evoppi.rest.entity;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.ToLongFunction;

import javax.ws.rs.core.UriBuilder;

import org.hamcrest.Factory;
import org.sing_group.evoppi.domain.entities.IsEqualToEntity;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

public class IsEqualToIdAndUri<T> extends IsEqualToEntity<T, IdAndUri> {
  private final BiFunction<BaseRestPathBuilder, T, URI> uriBuilder;
  private final ToLongFunction<T> idBuilder;
  
  public IsEqualToIdAndUri(T entity, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder, ToLongFunction<T> idBuilder) {
    super(entity);
    
    this.idBuilder = idBuilder;
    this.uriBuilder = uriBuilder;
  }

  @Override
  protected boolean matchesSafely(IdAndUri actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      if (this.getExpectedLong() != actual.getId()) {
        this.addDescription(String.format("actual login '%s' is different from expected login '%s'",
          actual.getId(), this.getExpectedLong()));
        
        return false;
      } else if (!this.checkUri(actual.getUri())) {
        this.addDescription(String.format("actual URI '%s' does not ends in '%s'",
          actual.getUri().getPath(), this.getExpectedUri().getPath()));
        
        return false;
      } else {
        return true;
      }
    }
  }
  
  private long getExpectedLong() {
    return this.idBuilder.applyAsLong(this.expected);
  }
  
  private URI getExpectedUri() {
    final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost");
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return this.uriBuilder.apply(pathBuilder, expected);
  }
  
  private boolean checkUri(URI actualUri) {
    final URI expectedUri = this.getExpectedUri();
    
    return actualUri.getPath().endsWith(expectedUri.getPath());
  }
  
  @Factory
  public static <T> IsEqualToIdAndUri<T> equalToIdAndUri(T entity, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder, ToLongFunction<T> idBuilder) {
    return new IsEqualToIdAndUri<T>(entity, uriBuilder, idBuilder);
  }
}
