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

package org.sing_group.evoppi.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.ListingOptions.SortField;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.evoppi.rest.entity.user.ResearcherData;
import org.sing_group.evoppi.rest.entity.user.ResearcherEditionData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.mapper.SecurityExceptionMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.spi.user.ResearcherResource;
import org.sing_group.evoppi.service.spi.user.ResearcherService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@Path("researcher")
@Api(
  value = "researcher",
  authorizations = @Authorization("basicAuth")
)
@ApiResponses({
  @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE),
  @ApiResponse(code = 403, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
})
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultResearcherResource implements ResearcherResource {
  @Inject
  private ResearcherService service;
  
  @Inject
  private UserMapper userMapper;
  
  @Context
  private UriInfo uriInfo;
  
  private BaseRestPathBuilder pathBuilder;
  
  @PostConstruct
  private void createPathBuilder() {
    this.pathBuilder = new BaseRestPathBuilder(this.uriInfo.getBaseUriBuilder());
  }
  
  @GET
  @Path("{login}")
  @ApiOperation(
    value = "Finds researchers by login.",
    response = ResearcherData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login} | 'login' should have a length between 1 and 100")
  )
  @Override
  public Response get(@PathParam("login") String login) {
    final Researcher user = this.service.get(login);

    return Response.ok(userMapper.toResearcherData(user)).build();
  }
  
  @GET
  @ApiOperation(
    value = "Returns all the researchers in the database.",
    response = ResearcherData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of researchers in the database.")
  )
  @Override
  public Response list(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") String order,
    @QueryParam("sort") @DefaultValue("NONE") SortDirection sort
  ) {
    final ListingOptions options;
    
    if (order == null || sort == null || sort == SortDirection.NONE) {
      options = new ListingOptions(start, end);
    } else {
      options = new ListingOptions(start, end, new SortField(order, sort));
    }
    
    final ResearcherData[] researchers = this.service.list(options)
      .map(userMapper::toResearcherData)
    .toArray(ResearcherData[]::new);
    
    return Response.ok(researchers)
      .header("X-Total-Count", this.service.count())
    .build();
  }
  
  @POST
  @ApiOperation(
    value = "Creates a new researcher.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new researcher created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  @Override
  public Response create(ResearcherEditionData data) {
    final Researcher researcher = this.service.create(userMapper.toResearcher(data));
    
    final URI userUri = this.pathBuilder.researcher(researcher).build();
    
    return Response.created(userUri).build();
  }
  
  @PUT
  @ApiOperation(
    value = "Modifies an existing researcher.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response update(
    ResearcherEditionData data
  ) {
    this.service.update(userMapper.toResearcher(data));
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing researcher.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
}
