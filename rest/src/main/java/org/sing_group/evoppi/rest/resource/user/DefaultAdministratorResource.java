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
package org.sing_group.evoppi.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.sing_group.evoppi.domain.dao.ListingOptions.sortedBetween;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.domain.entities.user.AdministratorListingField;
import org.sing_group.evoppi.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.evoppi.rest.entity.user.AdministratorData;
import org.sing_group.evoppi.rest.entity.user.AdministratorEditionData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.mapper.SecurityExceptionMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.spi.user.AdministratorResource;
import org.sing_group.evoppi.service.spi.user.AdministratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@Path("admin")
@Api(
  value = "admin",
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
public class DefaultAdministratorResource implements AdministratorResource {
  @Inject
  private AdministratorService service;
  
  @Inject
  private UserMapper mapper;
  
  @Context
  private UriInfo uriInfo;
  
  private BaseRestPathBuilder pathBuilder;
  
  @PostConstruct
  private void createPathBuilder() {
    this.pathBuilder = new BaseRestPathBuilder(this.uriInfo.getBaseUriBuilder());
  }
  
  @Override
  @GET
  @Path("{login}")
  @ApiOperation(
    value = "Finds administrators by login.",
    response = AdministratorData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login} | 'login' should have a length between 1 and 100")
  )
  public Response get(@PathParam("login") String login) {
    final Administrator user = this.service.get(login);

    return Response.ok(mapper.toAdministratorData(user)).build();
  }
  
  @Override
  @GET
  @ApiOperation(
    value = "Returns all the administrators in the database.",
    response = AdministratorData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of administrators in the database.")
  )
  public Response list(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") AdministratorListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    final ListingOptions<Administrator> options = sortedBetween(start, end, order, sort);
    
    final AdministratorData[] admins = this.service.list(options)
      .map(mapper::toAdministratorData)
    .toArray(AdministratorData[]::new);
    
    return Response.ok(admins)
      .header("X-Total-Count", this.service.count())
    .build();
  }
  
  @Override
  @POST
  @ApiOperation(
    value = "Creates a new administrator.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new administrator created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  public Response create(AdministratorEditionData data) {
    final Administrator admin = this.service.create(mapper.toAdministrator(data));
    
    final URI userUri = pathBuilder.admin(admin).build();
    
    return Response.created(userUri).build();
  }
  
  @Override
  @PUT
  @ApiOperation(
    value = "Modifies an existing administrator.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response update(
    AdministratorEditionData data
  ) {
    this.service.update(mapper.toAdministrator(data));
    
    return Response.ok().build();
  }
  
  @Override
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing administrator.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
}
