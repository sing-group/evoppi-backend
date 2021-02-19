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
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.ListingOptions.FilterField;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResultListingField;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultField;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResultListingField;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultFilteringOptionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.spi.bio.InteractionResource;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interaction")
@Api(value = "interaction")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultInteractionResource implements InteractionResource {
  private static String MULTIPLE_UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}(,[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})*";
  
  @Inject
  private InteractionService service;
  
  @Inject
  private ExecutionMapper executionMapper;
  
  @Inject
  private BioMapper bioMapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.bioMapper.setUriBuilder(uriBuilder);
    this.executionMapper.setUriBuilder(uriBuilder);
  }
  
  @GET
  @ApiOperation(
    value = "Calculates the interactions of a gene according to one or many interactomes. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations."
      + "If the request contains authentication headers, the analysis results will be associated with the"
      + "corresponding user.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response getInteractions(
    @QueryParam("gene") int geneId,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("referenceInteractome") int[] referenceInteractome,
    @QueryParam("targetInteractome") int[] targetInteractome,
    @QueryParam("maxDegree") @DefaultValue("1") int maxDegree,
    @QueryParam("evalue") @DefaultValue("0.05") double evalue,
    @QueryParam("maxTargetSeqs") @DefaultValue("1") int maxTargetSeqs,
    @QueryParam("minIdentity") @DefaultValue("0.95") double minimumIdentity,
    @QueryParam("minAlignmentLength") @DefaultValue("18") int minimumAlignmentLength
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.interaction().result(id).build().toString();
      
    final WorkEntity work;
    if (isSameSpeciesQuery(interactomes, referenceInteractome, targetInteractome)) {
      work = this.service.findSameSpeciesInteractions(geneId, interactomes, maxDegree, resultUriBuilder);
    } else {
      final BlastQueryOptions blastOptions = new BlastQueryOptions(evalue, maxTargetSeqs, minimumIdentity, minimumAlignmentLength);
      
      work = this.service.findDifferentSpeciesInteractions(
        geneId, referenceInteractome, targetInteractome, blastOptions, maxDegree, resultUriBuilder
      );
    }
    
    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }
  
  private static boolean isSameSpeciesQuery(int[] interactomes, int[] referenceInteractomes, int[] targetInteractomes) {
    final boolean hasInteractomes = interactomes.length > 0;
    final boolean hasReferenceInteractomes = referenceInteractomes.length > 0;
    final boolean hasTargetInteractomes = targetInteractomes.length > 0;
    
    if (hasInteractomes && (hasReferenceInteractomes || hasTargetInteractomes)) {
      throw new IllegalArgumentException("interactome can't be set at the same time as referenceInteractome and targetInterctome");
    } else if (hasInteractomes) {
      return true;
    } else if (hasReferenceInteractomes || hasTargetInteractomes) {
      if (!hasReferenceInteractomes)
        throw new IllegalArgumentException("Missing referenceInteractome parameter");
      if (!hasTargetInteractomes)
        throw new IllegalArgumentException("Missing targetInteractome parameter");

      return false;
    } else {
      throw new IllegalArgumentException("At least, one interactome or referenceInteractome/targetInteractome must be provided.");
    }
  }

  @GET
  @Path("result/different")
  @ApiOperation(
    value = "Returns a summary of the different species interactions whose id is provided.",
    response = DifferentSpeciesInteractionsResultSummaryData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listDifferentSpeciesResults(
    @QueryParam("ids") String ids,
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") DifferentSpeciesInteractionsResultListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    if (!ids.matches(MULTIPLE_UUID_PATTERN))
      throw new IllegalArgumentException("Invalid 'ids' format");
    
    final List<FilterField<DifferentSpeciesInteractionsResult>> filters =
      FilterField.buildFromUri(DifferentSpeciesInteractionsResultListingField.values(), this.uriInfo)
        .collect(toList());
    
    final ListingOptions<DifferentSpeciesInteractionsResult> listingOptions =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);
    
    final String[] idList = ids.split(",");
    final DifferentSpeciesInteractionsResultSummaryData[] results = 
      this.service.listDifferentSpeciesResult(idList, listingOptions)
        .map(bioMapper::toInteractionQueryResultSummary)
      .toArray(DifferentSpeciesInteractionsResultSummaryData[]::new);
    
    return Response.ok(results)
      .header("X-Total-Count", this.service.countDifferentSpeciesResult(idList, listingOptions))
    .build();
  }

  @GET
  @Path("result/same")
  @ApiOperation(
    value = "Returns a summary of the same species interactions whose id is provided.",
    response = SameSpeciesInteractionsResultSummaryData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listSameSpeciesResults(
    @QueryParam("ids") String ids,
    @QueryParam("start") Integer start,
    @QueryParam("end") Integer end,
    @QueryParam("order") SameSpeciesInteractionsResultListingField order,
    @QueryParam("sort") SortDirection sort
  ) {
    if (!ids.matches(MULTIPLE_UUID_PATTERN))
      throw new IllegalArgumentException("Invalid 'ids' format");
    
    final List<FilterField<SameSpeciesInteractionsResult>> filters =
      FilterField.buildFromUri(SameSpeciesInteractionsResultListingField.values(), this.uriInfo)
        .collect(toList());
    
    final ListingOptions<SameSpeciesInteractionsResult> listingOptions =
      ListingOptions.sortedAndFilteredBetween(start, end, order, sort, filters);
    
    final String[] idList = ids.split(",");
    final SameSpeciesInteractionsResultSummaryData[] results = 
      this.service.listSameSpeciesResult(idList, listingOptions)
        .map(bioMapper::toInteractionQueryResultSummary)
      .toArray(SameSpeciesInteractionsResultSummaryData[]::new);
    
    return Response.ok(results)
      .header("X-Total-Count", this.service.countSameSpeciesResult(idList, listingOptions))
    .build();
  }
  
  @GET
  @Path("result/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @ApiOperation(
    value = "Returns the result of a interaction calculus.",
    response = InteractionsResultData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response getInterationResult(
    @PathParam("id") String id,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") InteractionGroupResultField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection,
    @QueryParam("interactomeId") Integer interactomeId,
    @QueryParam("summarize") @DefaultValue("false") boolean summarize
  ) {
    if (this.service.isSameSpeciesResult(id)) {
      final SameSpeciesInteractionsResult result = this.service.getSameSpeciesResult(id);
      
      if (summarize) {
        return Response
          .ok(this.bioMapper.toInteractionQueryResultSummary(result))
        .build();
      } else {
        final InteractionsResultFilteringOptionsData filteringOptions = new InteractionsResultFilteringOptionsData(
          page, pageSize, orderField, sortDirection, interactomeId
        );
        
        return Response
          .ok(this.bioMapper.toInteractionQueryResult(result, filteringOptions))
        .build();
      }
    } else if (this.service.isDifferentSpeciesResult(id)) {
      final DifferentSpeciesInteractionsResult result = this.service.getDifferentSpeciesResult(id);

      if (summarize) {
        return Response
          .ok(this.bioMapper.toInteractionQueryResultSummary(result))
        .build();
      } else {
        final InteractionsResultFilteringOptionsData filteringOptions = new InteractionsResultFilteringOptionsData(
          page, pageSize, orderField, sortDirection, interactomeId
        );
        
        return Response
          .ok(this.bioMapper.toInteractionQueryResult(result, filteringOptions))
        .build();
      }
    } else {
      throw new IllegalArgumentException("Unknown interactions results id: " + id);
    }
  }

  @DELETE
  @Path("result/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @ApiOperation(
    value = "Deletes the result of a interaction calculus.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response deleteInterationResult(
    @PathParam("id") String id
  ) {
    this.service.deleteResult(id);
    
    return Response.ok().build();
  }

  @GET
  @Path("result/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interaction")
  @ApiOperation(
    value = "Returns the interactions of a interaction calculus.",
    response = InteractionsData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response getInterationResultInteractions(
    @PathParam("id") String id,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") InteractionGroupResultField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection,
    @QueryParam("interactomeId") Integer interactomeId
  ) {
    if (this.service.isSameSpeciesResult(id)) {
      final SameSpeciesInteractionsResult result = this.service.getSameSpeciesResult(id);
      
      final InteractionsResultFilteringOptionsData filteringOptions = new InteractionsResultFilteringOptionsData(
        page, pageSize, orderField, sortDirection, interactomeId
      );
      
      return Response
        .ok(this.bioMapper.toInteractionsResultData(result, filteringOptions))
      .build();
    } else if (this.service.isDifferentSpeciesResult(id)) {
      final DifferentSpeciesInteractionsResult result = this.service.getDifferentSpeciesResult(id);

      final InteractionsResultFilteringOptionsData filteringOptions = new InteractionsResultFilteringOptionsData(
        page, pageSize, orderField, sortDirection, interactomeId
      );
      
      return Response
        .ok(this.bioMapper.toInteractionsResultData(result, filteringOptions))
      .build();
    } else {
      throw new IllegalArgumentException("Unknown interactions results id: " + id);
    }
  }

  @GET
  @Path("result/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interactome/fasta")
  @Produces("text/x-fasta")
  @ApiOperation(
    value = "Returns a FASTA file with the genes in the result that belong to both query interactomes.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response getInterationResultInteractomeSingleFasta(
    @PathParam("id") String resultId,
    @QueryParam("addVersionSuffix") @DefaultValue("false") boolean includeVersionSuffix
  ) {
    if (this.service.isSameSpeciesResult(resultId)) {
      final String fasta = this.service.getSameSpeciesResultSingleFasta(resultId, includeVersionSuffix);
      
      return Response
        .ok(fasta)
      .build();
    } else if (this.service.isDifferentSpeciesResult(resultId)) {
      final String fasta = this.service.getDifferentSpeciesResultSingleFasta(resultId, includeVersionSuffix);
      
      return Response
        .ok(fasta)
      .build();
    } else {
      throw new IllegalArgumentException("Unknown interactions results id: " + resultId);
    }
  }

  @GET
  @Path("result/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interactome/{interactomeId: \\d+}/fasta")
  @Produces("text/x-fasta")
  @ApiOperation(
    value = "Returns a FASTA file with the genes in the result that belong to one of the query interactomes.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response getInterationResultInteractomeFasta(
    @PathParam("id") String resultId,
    @PathParam("interactomeId") int interactomeId,
    @QueryParam("addVersionSuffix") @DefaultValue("false") boolean includeVersionSuffix
  ) {
    if (this.service.isSameSpeciesResult(resultId)) {
      final String fasta = this.service.getSameSpeciesResultFasta(resultId, interactomeId, includeVersionSuffix);
      
      return Response
        .ok(fasta)
      .build();
    } else if (this.service.isDifferentSpeciesResult(resultId)) {
      final String fasta = this.service.getDifferentSpeciesResultFasta(resultId, interactomeId, includeVersionSuffix);
      
      return Response
        .ok(fasta)
      .build();
    } else {
      throw new IllegalArgumentException("Unknown interactions results id: " + resultId);
    }
  }
}
