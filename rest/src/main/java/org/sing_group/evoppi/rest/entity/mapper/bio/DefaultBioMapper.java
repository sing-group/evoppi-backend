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

import static java.util.Objects.requireNonNull;

import javax.enterprise.inject.Default;
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
import org.sing_group.evoppi.rest.entity.bio.BlastResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData.InteractingGenes;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

@Default
public class DefaultBioMapper implements BioMapper {
  private UriBuilder uriBuilder;

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
  public InteractionResultData toInteractionResultData(InteractionGroupResult interaction) {
    return new InteractionResultData(
      interaction.getGeneAId(),
      interaction.getGeneBId(),
      interaction.getInteractomeDegrees()
    );
  }
  
  @Override
  public SameSpeciesInteractionsResultData toInteractionQueryResult(SameSpeciesInteractionsResult result) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final IdAndUri[] interactomeIds = result.getQueryInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    final IdAndUri[] geneIds = result.getInteractions()
      .flatMapToInt(InteractionGroupResult::getGeneIds)
      .distinct()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.gene(id).build()))
    .toArray(IdAndUri[]::new);
    
    final InteractionResultData[] data = result.getInteractions()
      .map(this::toInteractionResultData)
    .toArray(InteractionResultData[]::new);
    
    return new SameSpeciesInteractionsResultData(
      result.getId(),
      result.getQueryGeneId(),
      result.getQueryMaxDegree(),
      interactomeIds,
      geneIds,
      data,
      result.getStatus()
    );
  }

  @Override
  public DifferentSpeciesInteractionsResultData toInteractionQueryResult(DifferentSpeciesInteractionsResult result) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final InteractionResultData[] interactionData = result.getInteractions()
      .map(this::toInteractionResultData)
    .toArray(InteractionResultData[]::new);
    
    final BlastResultData[] blastResults = result.getBlastResults()
      .map(this::toBlastResultData)
    .toArray(BlastResultData[]::new);
    
    final IdAndUri[] referenceGenes = result.getReferenceGeneIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.gene(id).build()))
    .toArray(IdAndUri[]::new);
    
    final IdAndUri[] targetGenes = result.getTargetGeneIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.gene(id).build()))
    .toArray(IdAndUri[]::new);
    
    final IdAndUri[] referenceInteractomeIds = result.getReferenceInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    final IdAndUri[] targetInteractomeIds = result.getTargetInteractomeIds()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    return new DifferentSpeciesInteractionsResultData(
      result.getId(),
      result.getQueryGeneId(),
      referenceInteractomeIds,
      targetInteractomeIds,
      result.getQueryMaxDegree(),
      referenceGenes,
      targetGenes,
      interactionData,
      blastResults,
      result.getStatus()
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
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final GeneNameData[] geneNames = gene.getNames()
      .map(this::toGeneNameData)
    .toArray(GeneNameData[]::new);
    
    final IdAndUri[] interactomes = gene.getInteractions()
      .map(Interaction::getInteractome)
      .mapToInt(Interactome::getId)
      .distinct()
      .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
    .toArray(IdAndUri[]::new);
    
    return new GeneNamesData(gene.getId(), geneNames, interactomes);
  }

  @Override
  public GeneNameData toGeneNameData(GeneNames geneNames) {
    return new GeneNameData(geneNames.getSource(), geneNames.getNames().toArray(String[]::new));
  }
}
