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

import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.resource.spi.bio.InteractomeResource;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactome")
@Api(value = "interactome")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
public class DefaultInteractomeResource implements InteractomeResource {
  @Inject
  private InteractomeService service;
  
  @Inject
  private BioMapper mapper;

  @Context
  private UriInfo uriInfo;

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds an interactome by identifier.",
    response = SpeciesData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interactome: {id}")
  )
  @Override
  public Response getInteractome(@PathParam("id") int id) {
    final Interactome interactome = this.service.getInteractome(id);
    
    return Response
      .ok(mapInteractomeToData(interactome))
    .build();
  }

  private InteractomeData mapInteractomeToData(final Interactome interactome) {
    return this.mapper.toInteractomeData(interactome, this.uriInfo.getBaseUriBuilder());
  }

}
