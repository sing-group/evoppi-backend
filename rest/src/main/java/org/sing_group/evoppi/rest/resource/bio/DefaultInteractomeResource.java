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

package org.sing_group.evoppi.rest.resource.bio;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.InteractomeResource;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactome")
@Api(value = "interactome")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultInteractomeResource implements InteractomeResource {
  @Inject
  private InteractomeService service;
  
  @Inject
  private BioMapper mapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    this.mapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds an interactome by identifier. If the query parameter 'includeInteractions' is set to true, the"
      + " 'genes' and 'interactions' fields are returned as part of the result. Otherwise, these fields are not "
      + "returned.",
    response = InteractomeWithInteractionsData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interactome: {id}")
  )
  @Override
  public Response getInteractome(
    @PathParam("id") int id,
    @DefaultValue("false") @QueryParam("includeInteractions") boolean includeInteractions
  ) {
    final Interactome interactome = this.service.getInteractome(id);
    
    if (includeInteractions) {
      return Response
        .ok(this.mapper.toInteractomeWithInteractionsData(interactome))
      .build();
    } else {
      return Response
        .ok(this.mapper.toInteractomeData(interactome))
      .build();
    }
  }

  @GET
  @ApiOperation(
    value = "Returns a list with all the interactome information.",
    response = InteractomeData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listInteractomes() {
    final InteractomeData[] interactomeData = this.service.listInteractomes()
            .map(this.mapper::toInteractomeData)
            .toArray(InteractomeData[]::new);

    return Response.ok(interactomeData).build();
  }
}
