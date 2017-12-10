package org.sing_group.evoppi.rest.resource.bio;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.rest.entity.bio.InteractionData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.bio.InteractionResource;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("interaction")
@Api(value = "interaction")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultInteractionResource implements InteractionResource {
  @Inject
  private InteractionService service;
  
  @Inject
  private BioMapper mapper;

  @Context
  private UriInfo uriInfo;
  
  @GET
  @ApiOperation(
    value = "Returns the interactions of a gene according to one or many interactomes.",
    response = InteractionData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response getInteractions(
    @QueryParam("gene") int geneId,
    @QueryParam("interactome") int[] interactomes
  ) {
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    final InteractionData[] interactions = this.service.findInteractionsByGene(geneId, interactomes)
      .map(interaction -> mapper.toInteractionData(interaction, this.uriInfo.getAbsolutePathBuilder()))
    .toArray(InteractionData[]::new);
    
    return Response.ok(interactions).build();
  }

}
