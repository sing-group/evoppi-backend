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

import static java.util.Arrays.stream;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.stream.Stream;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.query.GeneQueryOptions;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.GeneMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.GeneResource;
import org.sing_group.evoppi.service.spi.bio.GeneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("gene")
@Api(value = "gene")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultGeneResource implements GeneResource {
  @Inject
  private GeneService service;
  
  @Inject
  private GeneMapper mapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.mapper.setUriBuilder(uriBuilder);
  }
  
  @Path("{id: \\d+}")
  @GET
  @ApiOperation(
    value = "Return the data of a gene.",
    response = GeneData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown gene: {id}")
  )
  @Override
  public Response getGene(
    @PathParam("id") int id
  ) {
    return Response
      .ok(this.mapper.toGeneData(this.service.get(id)))
    .build();
  }

  @Path("{id: \\d+}/name")
  @GET
  @ApiOperation(
    value = "Returns the list of names of a gene.",
    response = GeneNamesData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown gene: {id}")
  )
  @Override
  public Response getGeneNames(
    @PathParam("id") int id
  ) {
    return Response
      .ok(this.mapper.toGeneNamesData(this.service.get(id)))
    .build();
  }

  @GET
  @ApiOperation(
    value = "Returns a list of genes. Query can be done by providing the gene identifiers or by providing a gene prefix"
      + " together with the interactome to which the gene should belong and the max results to return.",
    response = GeneData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listGenes(
    @QueryParam("ids") String ids,
    @QueryParam("prefix") String prefix,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("maxResults") @DefaultValue("10") int maxResults
  ) {
    final MultivaluedMap<String, String> queryParameters = this.uriInfo.getQueryParameters();
    
    final boolean isIdsQuery = queryParameters.containsKey("ids");
    final boolean isPrefixQuery = queryParameters.containsKey("prefix");
    
    if (isIdsQuery && isPrefixQuery) {
      throw new IllegalArgumentException("Query parameter 'ids' can't be used together with 'prefix'");
    } else if (isIdsQuery) {
      final GeneData[] genesData = stream(ids.split(","))
        .mapToInt(Integer::valueOf)
        .mapToObj(this.service::get)
        .map(this.mapper::toGeneData)
      .toArray(GeneData[]::new);
      
      return Response.ok(genesData).build();
    } else if (isPrefixQuery) {
      final GeneQueryOptions queryOptions = new GeneQueryOptions(prefix, interactomes, maxResults);
      final Stream<Gene> genes = this.service.find(queryOptions);
      
      final GeneData[] genesData = genes
        .map(this.mapper::toGeneData)
      .toArray(GeneData[]::new);
      
      return Response.ok(genesData).build();
    } else {
      throw new IllegalArgumentException("Missing query parameters in invocation");
    }
  }

  @Path("name")
  @GET
  @ApiOperation(
    value = "Returns a list of genes names. This list can be filtered with a prefix and, optionally a list of "
      + "interactomes to which the gene belongs. The maximum number of returned genes can also be set.",
    response = GeneNamesData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listGeneNames(
    @QueryParam("prefix") String prefix,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("maxResults") @DefaultValue("10") int maxResults
  ) {
    final GeneQueryOptions queryOptions = new GeneQueryOptions(prefix, interactomes, maxResults);
    final Stream<Gene> genes = this.service.find(queryOptions);
    
    final GeneNamesData[] geneNames = genes
      .map(this.mapper::toGeneNamesData)
    .toArray(GeneNamesData[]::new);
    
    return Response.ok(geneNames).build();
  }

}
