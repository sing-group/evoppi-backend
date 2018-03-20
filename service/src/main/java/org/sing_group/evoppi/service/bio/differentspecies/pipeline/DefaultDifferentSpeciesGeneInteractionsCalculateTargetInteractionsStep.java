/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static java.util.Arrays.stream;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;

@Transactional(REQUIRES_NEW)
public class DefaultDifferentSpeciesGeneInteractionsCalculateTargetInteractionsStep
implements DifferentSpeciesGeneInteractionsStep {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;

  @Inject
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;

  @Override
  public String getName() {
    return "Target genome interactions calculus";
  }
  
  @Override
  public int getOrder() {
    return 4;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getTargetInteractions().isPresent();
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    final Set<Integer> targtGeneIds = context.getBlastResults().orElseThrow(IllegalStateException::new)
      .mapToInt(BlastResult::getSseqid)
      .distinct()
      .boxed()
    .collect(toSet());
    
    final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    final Set<Interactome> targetInteractomes = configuration.getTargetInteractomes()
      .mapToObj(interactomeDao::getInteractome)
      .collect(toSet());
    
    final Gene[] targetGenes = targtGeneIds.stream()
      .map(this.geneDao::getGene)
    .toArray(Gene[]::new);
    
    final Stream<GeneInteraction> targetInteractions = getDirectGeneInteractons(targetInteractomes, targetGenes);
    
    return this.contextBuilderFactory.createBuilderFor(context)
      .setTargetInteractions(targetInteractions)
    .build();
  }
  
  private Stream<GeneInteraction> getDirectGeneInteractons(Set<Interactome> interactomes, Gene ... genes) {
    final Set<Gene> genesSet = stream(genes).collect(toSet());
    final Predicate<Interaction> hasValidGenes = interaction -> 
      genesSet.contains(interaction.getGeneA()) && genesSet.contains(interaction.getGeneB());
      
    final Function<Interactome, Stream<GeneInteraction>> geneInteractions =
      interactome -> interactome.getInteractions()
        .filter(hasValidGenes)
      .map(interaction -> new GeneInteraction(
        interaction.getGeneA().getId(),
        interaction.getGeneB().getId(),
        singletonMap(interactome.getId(), 1)
      ));
      
    final Function<Collection<GeneInteraction>, GeneInteraction> giMerger =
      gis -> gis.stream().reduce((gi1, gi2) -> {
        final Map<Integer, Integer> interactomeDegrees = new HashMap<>();
        interactomeDegrees.putAll(gi1.getInteractomeDegrees());
        interactomeDegrees.putAll(gi2.getInteractomeDegrees());
        
        return new GeneInteraction(gi1.getGeneAId(), gi1.getGeneBId(), interactomeDegrees);
      }).get();
      
    return interactomes.stream()
      .flatMap(geneInteractions)
      .collect(groupingBy(gi -> gi.getGeneAId() + "#" + gi.getGeneAId()))
      .values().stream()
      .map(giMerger);
  }
}
