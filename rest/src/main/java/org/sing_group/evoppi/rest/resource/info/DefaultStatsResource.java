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
package org.sing_group.evoppi.rest.resource.info;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.domain.entities.DatabaseInformation;
import org.sing_group.evoppi.rest.entity.info.StatsData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.info.StatsResource;
import org.sing_group.evoppi.service.spi.info.DatabaseInformationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("database")
@Api("database")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultStatsResource implements StatsResource {

  @Inject
  private DatabaseInformationService databaseInformationService;

  @GET
  @Path("stats")
  @ApiOperation(
    value = "Provides database statistic information.",
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
        this.databaseInformationService.getSpeciesCount(),
        this.databaseInformationService.getDatabaseInteractomesCount(),
        this.databaseInformationService.getPredictomesCount(),
        this.databaseInformationService.getGenesCount(),
        this.databaseInformationService.getInteractionsCount()
      )
    ).build();
  }
  
  @GET
  @Path("version")
  @ApiOperation(
    value = "Provides the current database version.",
    response = String.class,
    code = 200
    )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
    @ApiResponse(code = 400, message = "The database version is not available")
  })
  @Override
  public Response getCurrentDatabaseVersion() {
    Optional<DatabaseInformation> info = this.databaseInformationService.getDbVersion();
    if (info.isPresent()) {
      return Response.ok(
        info.get().getVersion()
      ).build();
    } else {
      throw new RuntimeException("The database version is not available");
    }
  }
}
