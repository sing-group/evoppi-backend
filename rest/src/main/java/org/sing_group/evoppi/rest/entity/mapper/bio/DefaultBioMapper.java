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

package org.sing_group.evoppi.rest.entity.mapper.bio;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.net.URI;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultListingOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;
import org.sing_group.evoppi.rest.entity.UuidAndUri;
import org.sing_group.evoppi.rest.entity.bio.BlastResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultFilteringOptionsData;
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
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.OrthologsManager;

@Default
@Transactional(MANDATORY)
public class DefaultBioMapper implements BioMapper {
  private UriBuilder uriBuilder;
  
  @PersistenceContext
  private EntityManager em;
  
  @Inject
  private GeneService geneService;
  
  @Inject
  private InteractionService interactionService;

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
      interactome.getDbSourceIdType().orElse(null),
      interactome.getNumOriginalInteractions().isPresent() ? interactome.getNumOriginalInteractions().getAsInt() : null,
      interactome.getNumUniqueOriginalInteractions().isPresent() ? interactome.getNumUniqueOriginalInteractions().getAsInt() : null,
      interactome.getNumUniqueOriginalGenes().isPresent() ? interactome.getNumUniqueOriginalGenes().getAsInt() : null,
      interactome.getNumInteractionsNotToUniProtKB().isPresent() ? interactome.getNumInteractionsNotToUniProtKB().getAsInt() : null,
      interactome.getNumGenesNotToUniProtKB().isPresent() ? interactome.getNumGenesNotToUniProtKB().getAsInt(): null,
      interactome.getNumInteractionsNotToGeneId().isPresent() ? interactome.getNumInteractionsNotToGeneId().getAsInt() : null,
      interactome.getNumGenesNotToGeneId().isPresent() ? interactome.getNumGenesNotToGeneId().getAsInt() : null,
      interactome.getNumFinalInteractions().isPresent() ? interactome.getNumFinalInteractions().getAsInt() : null,
      interactome.getProbFinalInteractions().isPresent() ? interactome.getProbFinalInteractions().getAsDouble() : null,
      interactome.getNumRemovedInterSpeciesInteractions().isPresent() ? interactome.getNumRemovedInterSpeciesInteractions().getAsInt() : null
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
      interactome.getDbSourceIdType().orElse(null),
      interactome.getNumOriginalInteractions().isPresent() ? interactome.getNumOriginalInteractions().getAsInt() : null,
      interactome.getNumUniqueOriginalInteractions().isPresent() ? interactome.getNumUniqueOriginalInteractions().getAsInt() : null,
      interactome.getNumUniqueOriginalGenes().isPresent() ? interactome.getNumUniqueOriginalGenes().getAsInt() : null,
      interactome.getNumInteractionsNotToUniProtKB().isPresent() ? interactome.getNumInteractionsNotToUniProtKB().getAsInt() : null,
      interactome.getNumGenesNotToUniProtKB().isPresent() ? interactome.getNumGenesNotToUniProtKB().getAsInt(): null,
      interactome.getNumInteractionsNotToGeneId().isPresent() ? interactome.getNumInteractionsNotToGeneId().getAsInt() : null,
      interactome.getNumGenesNotToGeneId().isPresent() ? interactome.getNumGenesNotToGeneId().getAsInt() : null,
      interactome.getNumFinalInteractions().isPresent() ? interactome.getNumFinalInteractions().getAsInt() : null,
      interactome.getProbFinalInteractions().isPresent() ? interactome.getProbFinalInteractions().getAsDouble() : null,
      interactome.getNumRemovedInterSpeciesInteractions().isPresent() ? interactome.getNumRemovedInterSpeciesInteractions().getAsInt() : null,
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
      gene.getDefaultName(),
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
    
    return new GeneNamesData(gene.getId(), gene.getDefaultName(), geneNames, interactomes);
  }

  @Override
  public GeneNameData toGeneNameData(GeneNames geneNames) {
    return new GeneNameData(geneNames.getSource(), geneNames.getNames().toArray(String[]::new));
  }

  @Override
  public SameSpeciesInteractionsResultData toInteractionQueryResult(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final Species species = result.getQuerySpecies();
      final IdNameAndUri speciesId = new IdNameAndUri(
        species.getId(), species.getName(), pathBuilder.species(species).build()
      );
      
      final Gene queryGene = result.getQueryGene();
      
      final IdNameAndUri queryGeneId = new IdNameAndUri(
        queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
      );
      
      final IdNameAndUri[] interactomeIds = result.getQueryInteractomes()
        .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
      .toArray(IdNameAndUri[]::new);
      
      return new SameSpeciesInteractionsResultData(
        result.getId(),
        queryGeneId,
        result.getQueryMaxDegree(),
        filteringOptions,
        speciesId,
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

    final Species species = result.getQuerySpecies();
    final IdNameAndUri speciesId = new IdNameAndUri(
      species.getId(), species.getName(), pathBuilder.species(species).build()
    );
    
    final Gene queryGene = result.getQueryGene();
    
    final IdNameAndUri queryGeneId = new IdNameAndUri(
      queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
    );

    final IdNameAndUri[] interactomeIds = result.getQueryInteractomes()
      .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
    .toArray(IdNameAndUri[]::new);
    
    final ResultRestPathBuilder interactionPathBuilder = pathBuilder.interaction().result(result.getId());
    final URI interactions = interactionPathBuilder.interaction().build();
    
    return new SameSpeciesInteractionsResultSummaryData(
      result.getId(),
      queryGeneId,
      result.getQueryMaxDegree(),
      speciesId,
      interactomeIds,
      interactions,
      (int) result.getInteractions().count(),
      result.getStatus()
    );
  }

  @Override
  public DifferentSpeciesInteractionsResultData toInteractionQueryResult(
    DifferentSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final Species referenceSpecies = result.getReferenceSpecies();
      final IdNameAndUri referenceSpeciesId = new IdNameAndUri(
        referenceSpecies.getId(), referenceSpecies.getName(), pathBuilder.species(referenceSpecies).build()
      );
      
      final Species targetSpecies = result.getTargetSpecies();
      final IdNameAndUri targetSpeciesId = new IdNameAndUri(
        targetSpecies.getId(), targetSpecies.getName(), pathBuilder.species(targetSpecies).build()
      );
      
      final Gene queryGene = result.getQueryGene();
      
      final IdNameAndUri queryGeneId = new IdNameAndUri(
        queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
      );
      
      final IdNameAndUri[] referenceInteractomeIds = result.getReferenceInteractomes()
        .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
      .toArray(IdNameAndUri[]::new);
      
      final IdNameAndUri[] targetInteractomeIds = result.getTargetInteractomes()
        .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
      .toArray(IdNameAndUri[]::new);
      
      return new DifferentSpeciesInteractionsResultData(
        result.getId(),
        queryGeneId,
        result.getQueryMaxDegree(),
        filteringOptions,
        referenceSpeciesId,
        targetSpeciesId,
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
    
    final Species referenceSpecies = result.getReferenceSpecies();
    final IdNameAndUri referenceSpeciesId = new IdNameAndUri(
      referenceSpecies.getId(), referenceSpecies.getName(), pathBuilder.species(referenceSpecies).build()
    );
    
    final Species targetSpecies = result.getTargetSpecies();
    final IdNameAndUri targetSpeciesId = new IdNameAndUri(
      targetSpecies.getId(), targetSpecies.getName(), pathBuilder.species(targetSpecies).build()
    );
    
    final Gene queryGene = result.getQueryGene();
    
    final IdNameAndUri queryGeneId = new IdNameAndUri(
      queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
    );

    final IdNameAndUri[] referenceInteractomeIds = result.getReferenceInteractomes()
      .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
    .toArray(IdNameAndUri[]::new);
    
    final IdNameAndUri[] targetInteractomeIds = result.getTargetInteractomes()
      .map(interactome -> new IdNameAndUri(interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()))
    .toArray(IdNameAndUri[]::new);

    return new DifferentSpeciesInteractionsResultSummaryData(
      result.getId(),
      queryGeneId,
      result.getQueryMaxDegree(),
      referenceSpeciesId,
      targetSpeciesId,
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
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final UuidAndUri resultId = new UuidAndUri(
        result.getId(),
        pathBuilder.interaction().result(result.getId()).build()
      );
  
      final InteractionGroupResultListingOptions options = createInteractionsResultFilter(filteringOptions);
      final InteractionResultData[] interactionData = interactionService.getInteractions(result, options)
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
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
      
      final UuidAndUri resultId = new UuidAndUri(
        result.getId(),
        pathBuilder.interaction().result(result.getId()).build()
      );
      final InteractionGroupResultListingOptions options =
        createInteractionsResultFilter(filteringOptions);
  
      final InteractionResultData[] interactionData = interactionService.getInteractions(result, options)
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
  
  private InteractionGroupResultListingOptions createInteractionsResultFilter(
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    final Integer start, end;
    
    if (filteringOptions.hasPagination()) {
      start = filteringOptions.getPage() * filteringOptions.getPageSize();
      end = start + filteringOptions.getPageSize() - 1;
    } else {
      start = end = null;
    }
    
    return new InteractionGroupResultListingOptions(
      start, end,
      filteringOptions.getOrderField(),
      filteringOptions.getSortDirection(),
      filteringOptions.getInteractomeId()
    );
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
