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
package org.sing_group.evoppi.rest.resource.execution;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.domain.entities.execution.WorkEntityListingField;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.execution.WorkResource;
import org.sing_group.evoppi.service.spi.execution.WorkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("work")
@Api("work")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain(allowedHeaders = {
  "X-Total-Count", "Location"
}, allowRequestHeaders = true)
public class DefaultWorkResource implements WorkResource {
  @Inject
  private WorkService service;

  @Inject
  private ExecutionMapper mapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    this.mapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @GET
  @ApiOperation(
    value = "Finds and returns a work by its identifier.", response = WorkData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown work: {id}")
  )
  @Override
  public Response get(
    @PathParam("id") String id
  ) {
    final WorkEntity work = this.service.get(id);

    return Response
      .ok(this.mapper.toWorkData(work))
      .build();
  }

  @Override
  @GET
  @ApiOperation(
    value = "Returns all the works.", response = WorkData.class, responseContainer = "List", code = 200, responseHeaders = @ResponseHeader(
      name = "X-Total-Count", description = "Total number of works in the database."
    )
  )
  public Response list(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") WorkEntityListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    final List<FilterField<WorkEntity>> filters =
      FilterField.buildFromUri(WorkEntityListingField.values(), uriInfo).collect(toList());
    
    final ListingOptions<WorkEntity> options =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);

    final WorkData[] admins =
      this.service.list(options)
        .map(mapper::toWorkData)
        .toArray(WorkData[]::new);

    return Response.ok(admins)
      .header("X-Total-Count", this.service.count(options))
      .build();
  }
}
