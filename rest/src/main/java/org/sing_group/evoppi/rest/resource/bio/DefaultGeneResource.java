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

import static java.util.stream.Collectors.toSet;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.GeneResource;
import org.sing_group.evoppi.service.spi.bio.GeneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

  @GET
  @ApiOperation(
    value = "Returns a list of genes. This list can be filtered with a prefix and, optionally a list of interactomes to"
      + " which the gene belongs. The maximum number of returned genes can also be set.",
    response = int.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listGenes(
    @QueryParam("prefix") int idPrefix,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("maxResults") @DefaultValue("10") int maxResults
  ) {
    final Set<Integer> interactomeIds = IntStream.of(interactomes)
      .distinct()
      .boxed()
    .collect(toSet());
    
    final Stream<Gene> genes = this.service.findByIdPrefixAndInteractome(idPrefix, interactomeIds, maxResults);
    
    final int[] geneNames = genes.mapToInt(Gene::getId).toArray();
    
    return Response.ok(geneNames).build();
  }

}
