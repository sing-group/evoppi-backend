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
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.domain.entities.bio.PredictomeListingField;
import org.sing_group.evoppi.rest.entity.bio.PredictomeData;
import org.sing_group.evoppi.rest.entity.bio.RestPredictomeCreationData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.PredictomeMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.PredictomeResource;
import org.sing_group.evoppi.service.spi.bio.PredictomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactome/predictome")
@Api(value = "interactome/predictome")
@Produces({
  APPLICATION_JSON, APPLICATION_XML, TEXT_PLAIN
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML, TEXT_PLAIN
})
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultPredictomeResource implements PredictomeResource {
  @Inject
  private PredictomeService service;

  @Inject
  private PredictomeMapper mapper;
  
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
    value = "Finds a predictome by identifier", 
    response = PredictomeData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown predictome: {id}")
  )
  @Override
  public Response get(
    @PathParam("id") int id
  ) {
    return Response
      .ok(this.mapper.toPredictomeData(this.service.get(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Returns a list with all the predictome information.",
    response = PredictomeData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response list(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") PredictomeListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    final List<FilterField<Predictome>> filters =
      FilterField.buildFromUri(PredictomeListingField.values(), this.uriInfo)
        .collect(toList());

    final ListingOptions<Predictome> options =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);

    final PredictomeData[] predictomeData =
      this.service
        .list(options)
        .map(this.mapper::toPredictomeData)
        .toArray(PredictomeData[]::new);

    return Response.ok(predictomeData)
      .header("X-Total-Count", this.service.count(options))
      .build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new predictome.",
    response = PredictomeData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Predictome already exists")
  )
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response create(RestPredictomeCreationData data) {
    return Response.ok(this.mapper.toPredictomeData(this.service.create(data))).build();
  }
}
