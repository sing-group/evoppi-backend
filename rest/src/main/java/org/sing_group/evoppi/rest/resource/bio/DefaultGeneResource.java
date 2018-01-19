/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.query.GeneQueryOptions;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
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
  private BioMapper bioMapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.bioMapper.setUriBuilder(uriBuilder);
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
      .ok(this.bioMapper.toGeneData(this.service.get(id)))
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
      .ok(this.bioMapper.toGeneNamesData(this.service.get(id)))
    .build();
  }

  @GET
  @ApiOperation(
    value = "Returns a list of genes. This list can be filtered with a prefix and, optionally a list of interactomes to"
      + " which the gene belongs. The maximum number of returned genes can also be set.",
    response = GeneData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listGenes(
    @QueryParam("prefix") String prefix,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("maxResults") @DefaultValue("10") int maxResults
  ) {
    final GeneQueryOptions queryOptions = new GeneQueryOptions(prefix, interactomes, maxResults);
    final Stream<Gene> genes = this.service.find(queryOptions);
    
    final GeneData[] genesData = genes
      .map(this.bioMapper::toGeneData)
    .toArray(GeneData[]::new);
    
    return Response.ok(genesData).build();
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
      .map(this.bioMapper::toGeneNamesData)
    .toArray(GeneNamesData[]::new);
    
    return Response.ok(geneNames).build();
  }

}
