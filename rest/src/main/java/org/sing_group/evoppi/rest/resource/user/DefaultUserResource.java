/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.user.Registration;
import org.sing_group.evoppi.domain.entities.user.RoleType;
import org.sing_group.evoppi.domain.entities.user.User;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.ResultUuids;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.mapper.SecurityExceptionMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.spi.user.UserResource;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("user")
@Api("user")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultUserResource implements UserResource {
  private Logger LOG = LoggerFactory.getLogger(DefaultUserResource.class);
  
  @Context
  private HttpServletRequest request;
  
  @Inject
  private UserService userService;
  
  @Inject
  private InteractionService interactionService;
  
  @Inject
  private BioMapper bioMapper;
  
  @Inject
  private UserMapper userMapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    this.bioMapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @GET
  @Path("role")
  @ApiOperation(
    value = "Checks the provided credentials",
    response = RoleType.class,
    code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
    @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE)
  })
  @Override
  public Response role(
    @QueryParam("login") String login,
    @QueryParam("password") String password
  ) {
    try {
      request.logout();
      request.login(login, password);
      
      final Optional<User> currentUser = this.userService.getCurrentUser();
      
      return Response.ok(
        User.getRoleName(currentUser.orElseThrow(IllegalArgumentException::new))
      ).build();
    } catch (IllegalArgumentException iae) {
      LOG.warn("No user in session after login", iae);
      
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } catch (ServletException e) {
      LOG.warn("Login attempt error", e);
      
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }
  
  @GET
  @Path("interaction/result/different")
  @ApiOperation(
    value = "Returns a summary of the different species interactions results that belong to an user.",
    response = DifferentSpeciesInteractionsResultSummaryData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listDifferentSpeciesResults() {
    final Optional<User> currentUser = this.userService.getCurrentUser();
    
    if (currentUser.isPresent()) {
      final DifferentSpeciesInteractionsResultSummaryData[] results = currentUser.get().getDifferentSpeciesResults()
        .map(bioMapper::toInteractionQueryResultSummary)
      .toArray(DifferentSpeciesInteractionsResultSummaryData[]::new);
      
      return Response.ok(results).build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }
  
  @GET
  @Path("interaction/result/same")
  @ApiOperation(
    value = "Returns a summary of the same species interactions results that belong to an user.",
    response = SameSpeciesInteractionsResultSummaryData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listSameSpeciesResults() {
    final Optional<User> currentUser = this.userService.getCurrentUser();
    
    if (currentUser.isPresent()) {
      final SameSpeciesInteractionsResultSummaryData[] results = currentUser.get().getSameSpeciesResults()
        .map(bioMapper::toInteractionQueryResultSummary)
      .toArray(SameSpeciesInteractionsResultSummaryData[]::new);
      
      return Response.ok(results).build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  
  @PUT
  @Path("interaction/result/different")
  @ApiOperation(
    value = "Links public different species results with the requresting user.",
    code = 200
  )
  @Override
  public Response claimDifferentSpeciesResults(ResultUuids uuids) {
    this.interactionService.linkDifferentSpeciesResultsToCurrentUser(uuids.getUuids());
    
    return Response.ok().build();
  }
  
  @PUT
  @Path("interaction/result/same")
  @ApiOperation(
    value = "Links public same species results with the requresting user.",
    code = 200
  )
  @Override
  public Response claimSameSpeciesResults(ResultUuids uuids) {
    this.interactionService.linkSameSpeciesResultsToCurrentUser(uuids.getUuids());
    
    return Response.ok().build();
  }

  @POST
  @Path("registration")
  @ApiOperation(
    value = "Returns a 201 code if registration was successful.",
    code = 201
  )
  @Override
  public Response register(UserRegistrationData registrationData) {
    final Registration registration = this.userMapper.toRegistration(registrationData);
    
    this.userService.register(registration);
    
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriInfo.getAbsolutePathBuilder());
    
    final URI uri = pathBuilder.user().registration(registration.getCode()).build();
    
    return Response.created(uri).build();
  }

  @POST
  @Consumes(MediaType.WILDCARD)
  @Path("registration/{code}")
  @ApiOperation(
    value = "Returns a 200 code if the registration is successfully confirmed.",
    code = 200
  )
  @Override
  public Response confirm(@PathParam("code") String code) {
    this.userService.confirm(code);
    
    return Response.ok().build();
  }
}
