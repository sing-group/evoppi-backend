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
package org.sing_group.evoppi.rest.resource.info;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.rest.entity.info.StatsData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.info.StatsResource;
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Path("stats")
@Api("stats")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultStatsResource implements StatsResource {
  @Inject
  private SpeciesService speciesService;
  
  @Inject
  private InteractomeService interactomeService;
  
  @Inject
  private GeneService geneService;
  
  @Inject
  private InteractionService interactionService;

  @GET
  @ApiOperation(
    value = "Provides database statistic information",
    response = StatsData.class,
    code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
  })
  @Override
  public Response get() {
    return Response.ok(
      new StatsData(
        this.speciesService.count(),
        this.interactomeService.count(),
        this.geneService.count(),
        this.interactionService.count()
      )
    ).build();
  }

}
