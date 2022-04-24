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
package org.sing_group.evoppi.rest.mapper;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.sing_group.evoppi.rest.filter.CrossDomainBuilder;
import org.sing_group.evoppi.rest.filter.CrossDomainConfiguration;
import org.sing_group.evoppi.rest.filter.DefaultCrossDomainConfiguration;

@Provider
public class ErrorResponseFilter implements ContainerResponseFilter {
  private CrossDomainBuilder corsBuilder;
  
  public ErrorResponseFilter() {
    this.corsBuilder = new CrossDomainBuilder();
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
  throws IOException {
    if (requestContext.getHeaderString("Origin") != null) {
      if (responseContext.getStatus() >= 400) {
        final MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();
        final CrossDomainConfiguration configuration = new DefaultCrossDomainConfiguration();

        this.corsBuilder.buildCorsHeaders(configuration, responseHeaders::putSingle, requestContext::getHeaderString);
      }
    }
  }

}
