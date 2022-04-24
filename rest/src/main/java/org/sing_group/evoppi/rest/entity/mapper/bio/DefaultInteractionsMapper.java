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

package org.sing_group.evoppi.rest.entity.mapper.bio;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.net.URI;
import java.util.Set;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultListingOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.rest.entity.IdNameAndUri;
import org.sing_group.evoppi.rest.entity.UuidAndUri;
import org.sing_group.evoppi.rest.entity.bio.BlastQueryOptionsData;
import org.sing_group.evoppi.rest.entity.bio.BlastResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultFilteringOptionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.GeneMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.InteractionsMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.route.ResultRestPathBuilder;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Default
@Transactional(MANDATORY)
public class DefaultInteractionsMapper implements InteractionsMapper {
  private UriBuilder uriBuilder;

  @PersistenceContext
  private EntityManager em;

  @Inject
  private GeneMapper geneMapper;

  @Inject
  private InteractionService interactionService;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
    this.geneMapper.setUriBuilder(uriBuilder);
  }

  @Override
  public SameSpeciesInteractionsResultData toInteractionQueryResult(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

      final Species species = result.getQuerySpecies();
      final IdNameAndUri speciesId =
        new IdNameAndUri(
          species.getId(), species.getName(), pathBuilder.species(species).build()
        );

      final Gene queryGene = result.getQueryGene();

      final IdNameAndUri queryGeneId =
        new IdNameAndUri(
          queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
        );

      final IdNameAndUri[] interactomeIds =
        result.getQueryInteractomes()
          .map(
            interactome -> new IdNameAndUri(
              interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
            )
          )
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
  public SameSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(
    SameSpeciesInteractionsResult result
  ) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    final Species species = result.getQuerySpecies();
    final IdNameAndUri speciesId =
      new IdNameAndUri(
        species.getId(), species.getName(), pathBuilder.species(species).build()
      );

    final Gene queryGene = result.getQueryGene();

    final IdNameAndUri queryGeneId =
      new IdNameAndUri(
        queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
      );

    final IdNameAndUri[] interactomeIds =
      result.getQueryInteractomes()
        .map(
          interactome -> new IdNameAndUri(
            interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
          )
        )
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
      final IdNameAndUri referenceSpeciesId =
        new IdNameAndUri(
          referenceSpecies.getId(), referenceSpecies.getName(), pathBuilder.species(referenceSpecies).build()
        );

      final Species targetSpecies = result.getTargetSpecies();
      final IdNameAndUri targetSpeciesId =
        new IdNameAndUri(
          targetSpecies.getId(), targetSpecies.getName(), pathBuilder.species(targetSpecies).build()
        );

      final Gene queryGene = result.getQueryGene();

      final IdNameAndUri queryGeneId =
        new IdNameAndUri(
          queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
        );

      final IdNameAndUri[] referenceInteractomeIds =
        result.getReferenceInteractomes()
          .map(
            interactome -> new IdNameAndUri(
              interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
            )
          )
          .toArray(IdNameAndUri[]::new);

      final IdNameAndUri[] targetInteractomeIds =
        result.getTargetInteractomes()
          .map(
            interactome -> new IdNameAndUri(
              interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
            )
          )
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
        result.getStatus(),
        this.toBlastQueryOptionsData(result.getBlastQueryOptions())
      );
    } finally {
      this.em.clear(); // Avoids unnecessary persistence check
    }
  }

  @Override
  public DifferentSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(
    DifferentSpeciesInteractionsResult result
  ) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    final ResultRestPathBuilder interactionPathBuilder = pathBuilder.interaction().result(result.getId());
    final URI interactions = interactionPathBuilder.interaction().build();

    final Species referenceSpecies = result.getReferenceSpecies();
    final IdNameAndUri referenceSpeciesId =
      new IdNameAndUri(
        referenceSpecies.getId(), referenceSpecies.getName(), pathBuilder.species(referenceSpecies).build()
      );

    final Species targetSpecies = result.getTargetSpecies();
    final IdNameAndUri targetSpeciesId =
      new IdNameAndUri(
        targetSpecies.getId(), targetSpecies.getName(), pathBuilder.species(targetSpecies).build()
      );

    final Gene queryGene = result.getQueryGene();

    final IdNameAndUri queryGeneId =
      new IdNameAndUri(
        queryGene.getId(), queryGene.getDefaultName(), pathBuilder.gene(queryGene).build()
      );

    final IdNameAndUri[] referenceInteractomeIds =
      result.getReferenceInteractomes()
        .map(
          interactome -> new IdNameAndUri(
            interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
          )
        )
        .toArray(IdNameAndUri[]::new);

    final IdNameAndUri[] targetInteractomeIds =
      result.getTargetInteractomes()
        .map(
          interactome -> new IdNameAndUri(
            interactome.getId(), interactome.getName(), pathBuilder.interactome(interactome).build()
          )
        )
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
      result.getStatus(),
      this.toBlastQueryOptionsData(result.getBlastQueryOptions())
    );
  }

  private InteractionResultData toInteractionResultData(InteractionGroupResult interaction) {
    return new InteractionResultData(
      interaction.getGeneAId(),
      interaction.getGeneA().getDefaultName(),
      interaction.getGeneBId(),
      interaction.getGeneB().getDefaultName(),
      interaction.getInteractomeDegreesById()
    );
  }
  
  private BlastQueryOptionsData toBlastQueryOptionsData(BlastQueryOptions blastQueryOptions) {
    return new BlastQueryOptionsData(
      blastQueryOptions.getEvalue(),
      blastQueryOptions.getMaxTargetSeqs(),
      blastQueryOptions.getMinimumIdentity(),
      blastQueryOptions.getMinimumAlignmentLength()
    );
  }

  @Override
  public SameSpeciesInteractionsData toInteractionsResultData(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  ) {
    try {
      final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

      final UuidAndUri resultId =
        new UuidAndUri(
          result.getId(),
          pathBuilder.interaction().result(result.getId()).build()
        );

      final InteractionGroupResultListingOptions options = createInteractionsResultFilter(filteringOptions);
      final InteractionResultData[] interactionData =
        interactionService.getInteractions(result, options)
          .map(this::toInteractionResultData)
          .toArray(InteractionResultData[]::new);

      return new SameSpeciesInteractionsData(
        resultId,
        filteringOptions,
        interactionData
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

      final UuidAndUri resultId =
        new UuidAndUri(
          result.getId(),
          pathBuilder.interaction().result(result.getId()).build()
        );
      final InteractionGroupResultListingOptions options =
        createInteractionsResultFilter(filteringOptions);

      final InteractionResultData[] interactionData =
        interactionService.getInteractions(result, options)
          .map(this::toInteractionResultData)
          .toArray(InteractionResultData[]::new);

      final Set<Integer> genes =
        Stream.of(interactionData)
          .flatMapToInt(InteractionResultData::getGenes)
          .distinct()
          .mapToObj(Integer::valueOf)
          .collect(toSet());

      final BlastResult[] blastResults =
        result.getBlastResults()
          .filter(blastResult -> genes.contains(blastResult.getQseqid()))
          .toArray(BlastResult[]::new);

      return new DifferentSpeciesInteractionsData(
        resultId,
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
