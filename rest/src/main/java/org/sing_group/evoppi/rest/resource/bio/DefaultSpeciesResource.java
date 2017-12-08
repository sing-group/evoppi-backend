package org.sing_group.evoppi.rest.resource.bio;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.mapper.SecurityExceptionMapper;
import org.sing_group.evoppi.rest.resource.spi.bio.SpeciesResource;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("species")
@Api(value = "species")
@ApiResponses({
  @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE),
  @ApiResponse(code = 403, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
})
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
public class DefaultSpeciesResource implements SpeciesResource {
  @Inject
  private SpeciesService service;
  
  @Inject
  private BioMapper mapper;

  @Context
  private UriInfo uriInfo;

  @GET
  @ApiOperation(
    value = "Returns a list with all the species information.",
    response = SpeciesData.class,
    code = 200
  )
  @Override
  public Response listSpecies() {
    final SpeciesData[] speciesData = this.service.listSpecies()
      .map(this::mapSpeciesToData)
    .toArray(SpeciesData[]::new);
    
    return Response.ok(speciesData).build();
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
      .ok(mapSpeciesToData(species))
    .build();
  }

  private SpeciesData mapSpeciesToData(final Species species) {
    return this.mapper.toSpeciesData(species, this.uriInfo.getBaseUriBuilder());
  }

}
