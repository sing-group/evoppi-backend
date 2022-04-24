/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.bio.differentspecies;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;

public class DefaultDifferentSpeciesGeneInteractionsContextBuilder
implements DifferentSpeciesGeneInteractionsContextBuilder {
  private final DifferentSpeciesGeneInteractionsPipeline pipeline;
  private final DifferentSpeciesGeneInteractionsConfiguration configuration;
  private final DifferentSpeciesGeneInteractionsEventManager eventManager;
  
  private Map<Integer, Set<HasGeneInteractionIds>> referenceInteractions;
  private Set<HasGeneInteractionIds> referenceCompletedInteractions;
  
  private Path referenceFastaPath;
  private Path targetFastaPath;
  
  private Set<BlastResult> blastResults;
  
  private Map<Integer, Set<HasGeneInteractionIds>> targetInteractions;
  private Set<HasGeneInteractionIds> targetCompletedInteractions;

  DefaultDifferentSpeciesGeneInteractionsContextBuilder(DifferentSpeciesGeneInteractionsContext context) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getReferenceInteractionsByDegree().map(HashMap::new).orElse(null),
      context.getReferenceCompletedInteractions().map(ri -> ri.collect(toSet())).orElse(null),
      context.getReferenceFastaPath().orElse(null),
      context.getTargetFastaPath().orElse(null),
      context.getBlastResults().map(br -> br.collect(toSet())).orElse(null),
      context.getTargetInteractionsByDegree().map(HashMap::new).orElse(null),
      context.getTargetCompletedInteractions().map(tci -> tci.collect(toSet())).orElse(null)
    );
  }
  
  DefaultDifferentSpeciesGeneInteractionsContextBuilder(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null, null, null, null, null, null);
  }
  
  private DefaultDifferentSpeciesGeneInteractionsContextBuilder(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager,
    Map<Integer, Set<HasGeneInteractionIds>> referenceInteractions,
    Set<HasGeneInteractionIds> referenceCompletedInteractions,
    Path referenceFastaPath,
    Path targetFastaPath,
    Set<BlastResult> blastResults,
    Map<Integer, Set<HasGeneInteractionIds>> targetInteractions,
    Set<HasGeneInteractionIds> targetCompletedInteractions
  ) {
    this.pipeline = requireNonNull(pipeline);
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    this.referenceInteractions = referenceInteractions;
    this.blastResults = blastResults;
    this.targetInteractions = targetInteractions;
    this.targetCompletedInteractions = targetCompletedInteractions;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder addReferenceInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  ) {
    if (this.referenceInteractions == null)
      this.referenceInteractions = new HashMap<>();
    
    final Set<HasGeneInteractionIds> interactionsSet = interactions.collect(toSet());
    
    if (!interactionsSet.isEmpty()) {
      this.referenceInteractions.merge(degree, interactionsSet, (prev, curr) -> {
        prev.addAll(curr);
        
        return prev;
      });
    }
    
    return this;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder addReferenceCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  ) {
    if (this.referenceCompletedInteractions == null)
      this.referenceCompletedInteractions = new HashSet<>();
    
    interactions.forEach(this.referenceCompletedInteractions::add);
    
    return this;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceFastaPath(Path referenceFastaPath) {
    this.referenceFastaPath = referenceFastaPath;
    
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetFastaPath(Path targetFastaPath) {
    this.targetFastaPath = targetFastaPath;
    
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Stream<BlastResult> blastResult) {
    this.blastResults = blastResult.collect(toSet());
    
    return this;
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder addTargetInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  ) {
    if (this.targetInteractions == null)
      this.targetInteractions = new HashMap<>();

    final Set<Integer> frontierGeneIds = this.getTargetFrontierGenes(degree - 1)
      .boxed()
    .collect(toSet());
    
    final Predicate<HasGeneInteractionIds> isValidInteraction = interaction ->
      interaction.getGeneIds().anyMatch(frontierGeneIds::contains);
    
    final Set<HasGeneInteractionIds> interactionsSet = interactions
      .filter(isValidInteraction)
    .collect(toSet());
    
    if (!interactionsSet.isEmpty()) {
      this.targetInteractions.merge(degree, interactionsSet, (prev, curr) -> {
        prev.addAll(curr);
        
        return prev;
      });
    }
    
    return this;
  }
  
  // Frontier genes from a degree are those that will generate the next degree
  // interactions. I.E. the genes that are not related with other genes with
  // a lower degree interaction.
  // For example, in the relation: Q <-1-> A <-2-> B <-3-> C
  // where Q is the query gene, the frontier gene for degree 2 is B.
  private IntStream getTargetFrontierGenes(int degree) {
    if (degree < 0) {
      throw new IllegalArgumentException("Only non-negative degrees are accepted");
    } else if (degree == 0) {
      return IntStream.of(this.configuration.getQueryGeneId());
    } else if (!this.targetInteractions.containsKey(degree)) {
      return IntStream.empty();
    } else {
      final Set<Integer> lowerDegreeFrontier = this.getTargetFrontierGenes(degree - 1)
        .boxed()
      .collect(toSet());
      
      return this.targetInteractions.get(degree).stream()
        .flatMapToInt(HasGeneInteractionIds::getGeneIds)
        .distinct()
      .filter(geneId -> !lowerDegreeFrontier.contains(geneId));
    }
  }

  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractionsCalculated() {
    if (this.targetInteractions == null)
      this.targetInteractions = emptyMap();
    
    return this;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder addTargetCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  ) {
    if (this.targetCompletedInteractions == null)
      this.targetCompletedInteractions = new HashSet<>();
    
    interactions.forEach(this.targetCompletedInteractions::add);
    
    return this;
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContext build() {
    return new DefaultDifferentSpeciesGeneInteractionsContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.referenceInteractions,
      this.referenceCompletedInteractions,
      this.referenceFastaPath,
      this.targetFastaPath,
      this.blastResults,
      this.targetInteractions,
      this.targetCompletedInteractions
    );
  }
}
