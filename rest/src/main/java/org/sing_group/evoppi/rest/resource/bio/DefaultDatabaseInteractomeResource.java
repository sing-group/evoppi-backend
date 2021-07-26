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

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractomeListingField;
import org.sing_group.evoppi.rest.entity.bio.DatabaseInteractomeData;
import org.sing_group.evoppi.rest.entity.bio.RestDatabaseInteractomeCreationData;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.DatabaseInteractomeMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.DatabaseInteractomeResource;
import org.sing_group.evoppi.service.spi.bio.DatabaseInteractomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactome/database")
@Api(value = "interactome/database")
@Produces({
  APPLICATION_JSON, APPLICATION_XML, TEXT_PLAIN
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML, TEXT_PLAIN
})
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultDatabaseInteractomeResource implements DatabaseInteractomeResource {
  @Inject
  private DatabaseInteractomeService service;

  @Inject
  private DatabaseInteractomeMapper mapper;
  
  @Inject
  private ExecutionMapper executionMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    this.mapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
    this.executionMapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds a database interactome by identifier", 
    response = DatabaseInteractomeData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown database interactome: {id}")
  )
  @Override
  public Response get(
    @PathParam("id") int id
  ) {
    return Response
      .ok(this.mapper.toDatabaseInteractomeData(this.service.get(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Returns a list with all the interactome information.",
    response = DatabaseInteractomeData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response list(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") DatabaseInteractomeListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    final List<FilterField<DatabaseInteractome>> filters =
      FilterField.buildFromUri(DatabaseInteractomeListingField.values(), this.uriInfo)
        .collect(toList());

    final ListingOptions<DatabaseInteractome> options =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);

    final DatabaseInteractomeData[] interactomeData =
      this.service
        .list(options)
        .map(this.mapper::toDatabaseInteractomeData)
        .toArray(DatabaseInteractomeData[]::new);

    return Response.ok(interactomeData)
      .header("X-Total-Count", this.service.count(options))
      .build();
  }
  
  @POST
  @ApiOperation(
    value = "Creates a new database interactome. "
      + "The processing is done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Database interactome name already exists")
  )
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response create(RestDatabaseInteractomeCreationData data) {
    return Response.ok(this.executionMapper.toWorkData(this.service.create(data))).build();
  }
}
