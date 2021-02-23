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
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.File;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.SpeciesListingField;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.SpeciesResource;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("species")
@Api(value = "species")
@Produces({ APPLICATION_JSON, APPLICATION_XML, APPLICATION_OCTET_STREAM })
@Consumes({ APPLICATION_JSON, APPLICATION_XML, APPLICATION_OCTET_STREAM })
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultSpeciesResource implements SpeciesResource {
  @Inject
  private SpeciesService service;
  
  @Inject
  private BioMapper mapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    this.mapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @GET
  @ApiOperation(
    value = "Returns a list with all the species information.",
    response = SpeciesData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listSpecies(
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") SpeciesListingField order,
    @QueryParam("sort") SortDirection sort  
  ) {
    final List<FilterField<Species>> filters =
      FilterField.buildFromUri(SpeciesListingField.values(), this.uriInfo)
        .collect(toList());

    final ListingOptions<Species> options =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);

    final SpeciesData[] speciesData = this.service.listSpecies(options)
      .map(this.mapper::toSpeciesData)
    .toArray(SpeciesData[]::new);
    
    return Response.ok(speciesData)
      .header("X-Total-Count", this.service.count(options))
    .build();
  }

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds a species by identifier.",
    response = SpeciesData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown species: {id}")
  )
  @Override
  public Response getSpecies(@PathParam("id") int id) {
    final Species species = this.service.getSpecies(id);
    
    return Response
      .ok(this.mapper.toSpeciesData(species))
    .build();
  }
  
  @GET
  @Path("{id}/fasta")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @ApiOperation(
    value = "Gets the FASTA file with the genes of one species.",
    response = SpeciesData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown species: {id}")
  )
  @Override
  public Response getSpeciesFasta(@PathParam("id") int id) {
    final Species species = this.service.getSpecies(id);

    File fasta = this.mapper.toSpeciesFasta(species);

    return Response.ok(fasta, MediaType.APPLICATION_OCTET_STREAM)
      .header("Content-Disposition", "attachment; filename=\"" + fasta.getName() + "\"") // optional
      .build();
  }
}
