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
package org.sing_group.evoppi.rest.entity.mapper.bio;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.net.URI;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.UuidAndUri;
import org.sing_group.evoppi.rest.entity.bio.BlastResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultFilteringOptions;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData.InteractingGenes;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.route.ResultRestPathBuilder;
import org.sing_group.evoppi.service.bio.BlastResultOrthologsManager;
import org.sing_group.evoppi.service.bio.entity.InteractionsResultFilter;
import org.sing_group.evoppi.service.bio.entity.InteractionsResultFilter.InteractionsResultFilterBuilder;
import org.sing_group.evoppi.service.bio.entity.InteractionsResultFilter.InteractionsResultFilterOrderBuilder;
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.OrthologsManager;

@Default
public class DefaultBioMapper implements BioMapper {
  private UriBuilder uriBuilder;
  
  @PersistenceContext
  private EntityManager em;
  
  @Inject
  private GeneService geneService;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
  }
  
  @Override
  public SpeciesData toSpeciesData(Species species) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new SpeciesData(
      species.getId(),
      species.getName(),
      species.getInteractomes()
        .map(interactome -> new IdAndUri(
          interactome.getId(),
          pathBuilder.interactome(interactome).build()
        ))
      .toArray(IdAndUri[]::new)
    );
  }

  public InteractomeData toInteractomeData(Interactome interactome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new InteractomeData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpecies().getId(),
        pathBuilder.species(interactome.getSpecies()).build()
      ),
      interactome.getDbSourceIdType(),
      interactome.getNumOriginalInteractions(),
      interactome.getNumUniqueOriginalInteractions(),
      interactome.getNumUniqueOriginalGenes(),
      interactome.getNumInteractionsNotToUniProtKB(),
      interactome.getNumGenesNotToUniProtKB(),
      interactome.getNumInteractionsNotToGeneId(),
      interactome.getNumGenesNotToGeneId(),
      interactome.getNumFinalInteractions(),
      interactome.getProbFinalInteractions()
    );
  }
  
  @Override
  public InteractomeWithInteractionsData toInteractomeWithInteractionsData(Interactome interactome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final IdAndUri[] genes = interactome.getInteractions()
      .flatMap(Interaction::getGenes)
      .distinct()
      .map(gene -> new IdAndUri(gene.getId(), pathBuilder.gene(gene).build()))
    .toArray(IdAndUri[]::new);
    
    final InteractingGenes[] interactions = interactome.getInteractions()
      .map(interaction -> new InteractingGenes(interaction.getGeneAId(), interaction.getGeneBId()))
    .toArray(InteractingGenes[]::new);
    
    return new InteractomeWithInteractionsData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpecies().getId(),
        pathBuilder.species(interactome.getSpecies()).build()
      ),
      interactome.getDbSourceIdType(),
      interactome.getNumOriginalInteractions(),
      interactome.getNumUniqueOriginalInteractions(),
      interactome.getNumUniqueOriginalGenes(),
      interactome.getNumInteractionsNotToUniProtKB(),
      interactome.getNumGenesNotToUniProtKB(),
      interactome.getNumInteractionsNotToGeneId(),
      interactome.getNumGenesNotToGeneId(),
      interactome.getNumFinalInteractions(),
      interactome.getProbFinalInteractions(),
      genes,
      interactions
    );
  }

  public InteractingGenes toInteractingGenes(Interaction interaction) {
    return new InteractingGenes(
      interaction.getGeneAId(),
      interaction.getGeneBId()
    );
  }
  
  @Override
  public GeneData toGeneData(Gene gene) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new GeneData(
      gene.getId(),
      new IdAndUri(gene.getSpecies().getId(), pathBuilder.species(gene.getSpecies()).build()),
      gene.getNames()
        .map(this::toGeneNameData)
      .toArray(GeneNameData[]::new),
      gene.getSequences().toArray(String[]::new)
    );
  }

  @Override
  public GeneNamesData toGeneNamesData(Gene gene) {
    return this.toGeneNamesData(gene, __ -> true);
  }

  private GeneNamesData toGeneNamesData(Gene gene, IntPredicate interactomeFilter) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final GeneNameData[] geneNames = gene.getNames()
      .map(this::toGeneNameData)
    .toArray(GeneNameData[]::new);
    
    final IdAndUri[] interactomes = gene.getInteractions()
      .map(Interaction::getInteractome)
      .mapToInt(Interactome::getId)
      .filter(interactomeFilter)
      .distinct()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    return new GeneNamesData(gene.getId(), geneNames, interactomes);
  }

  @Override
  public GeneNameData toGeneNameData(GeneNames geneNames) {
    return new GeneNameData(geneNames.getSource(), geneNames.getNames().toArray(String[]::new));
  }

  @Override
  public SameSpeciesInteractionsResultData toInteractionQueryResult(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptions filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final IdAndUri[] interactomeIds = result.getQueryInteractomeIds()
        .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
      .toArray(IdAndUri[]::new);
      
      return new SameSpeciesInteractionsResultData(
        result.getId(),
        result.getQueryGeneId(),
        result.getQueryMaxDegree(),
        filteringOptions,
        interactomeIds,
        this.toInteractionsResultData(result, filteringOptions),
        (int) result.getInteractions().count(),
        result.getStatus()
      );
    } finally {
      this.em.clear(); // Avoids unnecessary persistence check
    }
  }
  
  @Override
  public SameSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(SameSpeciesInteractionsResult result) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final IdAndUri[] interactomeIds = result.getQueryInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    final ResultRestPathBuilder interactionPathBuilder = pathBuilder.interaction().result(result.getId());
    final URI interactions = interactionPathBuilder.interaction().build();
    
    return new SameSpeciesInteractionsResultSummaryData(
      result.getId(),
      result.getQueryGeneId(),
      result.getQueryMaxDegree(),
      interactomeIds,
      interactions,
      (int) result.getInteractions().count(),
      result.getStatus()
    );
  }

  @Override
  public DifferentSpeciesInteractionsResultData toInteractionQueryResult(
    DifferentSpeciesInteractionsResult result,
    InteractionsResultFilteringOptions filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final IdAndUri[] referenceInteractomeIds = result.getReferenceInteractomeIds()
        .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
      .toArray(IdAndUri[]::new);
      
      final IdAndUri[] targetInteractomeIds = result.getTargetInteractomeIds()
        .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
      .toArray(IdAndUri[]::new);
      
      return new DifferentSpeciesInteractionsResultData(
        result.getId(),
        result.getQueryGeneId(),
        result.getQueryMaxDegree(),
        filteringOptions,
        referenceInteractomeIds,
        targetInteractomeIds,
        this.toInteractionsResultData(result, filteringOptions),
        (int) result.getInteractions().count(),
        result.getStatus()
      );
    } finally {
      this.em.clear(); // Avoids unnecessary persistence check
    }
  }
  
  @Override
  public DifferentSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(DifferentSpeciesInteractionsResult result) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final ResultRestPathBuilder interactionPathBuilder = pathBuilder.interaction().result(result.getId());
    final URI interactions = interactionPathBuilder.interaction().build();
    
    final IdAndUri[] referenceInteractomeIds = result.getReferenceInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    final IdAndUri[] targetInteractomeIds = result.getTargetInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);

    return new DifferentSpeciesInteractionsResultSummaryData(
      result.getId(),
      result.getQueryGeneId(),
      result.getQueryMaxDegree(),
      referenceInteractomeIds,
      targetInteractomeIds,
      interactions,
      (int) result.getInteractions().count(),
      result.getStatus()
    );
  }
  
  @Override
  public InteractionResultData toInteractionResultData(InteractionGroupResult interaction) {
    return new InteractionResultData(
      interaction.getGeneAId(),
      interaction.getGeneBId(),
      interaction.getInteractomeDegreesById()
    );
  }

  @Override
  public SameSpeciesInteractionsData toInteractionsResultData(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptions filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final UuidAndUri resultId = new UuidAndUri(
        result.getId(),
        pathBuilder.interaction().result(result.getId()).build()
      );
  
      final InteractionsResultFilter resultFilter = createInteractionsResultFilter(filteringOptions);
      final InteractionResultData[] interactionData = resultFilter.filter(result.getInteractions())
        .map(this::toInteractionResultData)
      .toArray(InteractionResultData[]::new);
      
      final GeneNamesData[] genes = Stream.of(interactionData)
        .flatMapToInt(InteractionResultData::getGenes)
        .distinct()
        .mapToObj(this.geneService::get)
        .map(gene -> this.toGeneNamesData(gene, result::hasInteractome))
      .toArray(GeneNamesData[]::new);
      
      return new SameSpeciesInteractionsData(
        resultId,
        filteringOptions,
        interactionData,
        genes
      );
    } finally {
      this.em.clear(); // Avoids unnecessary persistence check
    }
  }
  
  @Override
  public DifferentSpeciesInteractionsData toInteractionsResultData(
    DifferentSpeciesInteractionsResult result,
    InteractionsResultFilteringOptions filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final UuidAndUri resultId = new UuidAndUri(
        result.getId(),
        pathBuilder.interaction().result(result.getId()).build()
      );
  
      final InteractionsResultFilter resultFilter = createInteractionsResultFilter(filteringOptions);
      final InteractionResultData[] interactionData = resultFilter.filter(result.getInteractions())
        .map(this::toInteractionResultData)
      .toArray(InteractionResultData[]::new);
      
      final Set<Integer> genes = Stream.of(interactionData)
        .flatMapToInt(InteractionResultData::getGenes)
        .distinct()
        .mapToObj(Integer::valueOf)
      .collect(toSet());
      
      final BlastResult[] blastResults = result.getBlastResults()
        .filter(blastResult -> genes.contains(blastResult.getQseqid()))
      .toArray(BlastResult[]::new);
      
      final GeneNamesData[] referenceGenes = genes.stream()
        .filter(result::hasReferenceGene)
        .sorted()
        .map(this.geneService::get)
        .map(gene -> this.toGeneNamesData(gene, result::hasInteractome))
      .toArray(GeneNamesData[]::new);
  
      final OrthologsManager orthologsManager = new BlastResultOrthologsManager(stream(blastResults));
      final GeneNamesData[] targetGenes = genes.stream()
        .flatMapToInt(orthologsManager::getOrthologsForReferenceGene)
        .sorted()
        .mapToObj(this.geneService::get)
        .map(gene -> this.toGeneNamesData(gene, result::hasInteractome))
      .toArray(GeneNamesData[]::new);
      
      return new DifferentSpeciesInteractionsData(
        resultId,
        referenceGenes,
        targetGenes,
        filteringOptions,
        stream(blastResults).map(this::toBlastResultData).toArray(BlastResultData[]::new),
        interactionData
      );
    } finally {
      this.em.clear(); // Avoids unnecessary persistence check
    }
  }
  
  private InteractionsResultFilter createInteractionsResultFilter(
    InteractionsResultFilteringOptions filteringOptions
  ) {
    InteractionsResultFilterBuilder builder = InteractionsResultFilter.builder();
    
    if (filteringOptions.hasPagination()) {
      builder = builder.paginated(filteringOptions.getPage(), filteringOptions.getPageSize());
    }
  
    if (filteringOptions.hasOrder()) {
      final InteractionsResultFilterOrderBuilder orderBuilder = builder.sort(filteringOptions.getSortDirection());
      
      switch(filteringOptions.getOrderField()) {
        case GENE_A_ID:
          builder = orderBuilder.byGeneAId();
          break;
        case GENE_B_ID:
          builder = orderBuilder.byGeneBId();
          break;
        case GENE_A_NAME:
          builder = orderBuilder.byGeneAName(id -> this.geneService.get(id).getRepresentativeName());
          break;
        case GENE_B_NAME:
          builder = orderBuilder.byGeneBName(id -> this.geneService.get(id).getRepresentativeName());
          break;
        case INTERACTOME:
          builder = orderBuilder.byDegreeInInteractome(filteringOptions.getInteractomeId());
          break;
      }
    }
    
    return builder.build();
  }

  private BlastResultData toBlastResultData(BlastResult result) {
    return new BlastResultData(
      result.getQseqid(),
      result.getQseqversion(),
      result.getSseqid(),
      result.getSseqversion(),
      result.getPident(),
      result.getLength(),
      result.getMismatch(),
      result.getGapopen(),
      result.getQstart(),
      result.getQend(),
      result.getSstart(),
      result.getSend(),
      result.getEvalue(),
      result.getBitscore()
    );
  }
}
