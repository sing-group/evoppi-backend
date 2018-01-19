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

import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractionData;
import org.sing_group.evoppi.rest.entity.bio.InteractionQueryResult;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.entity.user.IdAndUri;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.service.entity.bio.InteractionGroup;

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

  @Override
  public InteractomeData toInteractomeData(Interactome interactome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new InteractomeData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpecies().getId(),
        pathBuilder.species(interactome.getSpecies()).build()
      )
    );
  }

  @Override
  public InteractionData toInteractionData(InteractionGroup interactions) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    final Stream<Interactome> interactomes = interactions.getInteractomes();
    final Gene geneA = interactions.getGeneA();
    final Gene geneB = interactions.getGeneB();
    
    return new InteractionData(
      new IdAndUri(geneA.getId(), pathBuilder.gene(geneA).build()),
      new IdAndUri(geneB.getId(), pathBuilder.gene(geneB).build()),
      interactions.getDegree(),
      interactomes
        .map(interactome -> new IdAndUri(interactome.getId(), pathBuilder.interactome(interactome).build()))
      .toArray(IdAndUri[]::new)
    );
  }
  
  @Override
  public InteractionData toInteractionData(InteractionGroupResult interaction) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new InteractionData(
      new IdAndUri(interaction.getGeneAId(), pathBuilder.gene(interaction.getGeneAId()).build()),
      new IdAndUri(interaction.getGeneBId(), pathBuilder.gene(interaction.getGeneBId()).build()),
      interaction.getDegree(),
      interaction.getInteractomeIds()
        .map(interactomeId -> new IdAndUri(interactomeId, pathBuilder.interactome(interactomeId).build()))
      .toArray(IdAndUri[]::new)
    );
  }
  
  @Override
  public InteractionQueryResult toInteractionQueryResult(InteractionsResult result) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new InteractionQueryResult(
      new IdAndUri(result.getId(), pathBuilder.interaction().result(result.getId()).build()),
      new IdAndUri(result.getQueryGeneId(), pathBuilder.gene(result.getQueryGeneId()).build()),
      result.getQueryMaxDegree(),
      result.getQueryInteractomeIds()
        .mapToObj(id -> new IdAndUri(id, pathBuilder.interactome(id).build()))
      .toArray(IdAndUri[]::new),
      result.getInteractions()
        .map(interaction -> this.toInteractionData(interaction))
      .toArray(InteractionData[]::new),
      result.getStatus()
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
    return new GeneNamesData(gene.getId(), gene.getNames()
      .map(this::toGeneNameData)
    .toArray(GeneNameData[]::new));
  }

  @Override
  public GeneNameData toGeneNameData(GeneNames geneNames) {
    return new GeneNameData(geneNames.getSource(), geneNames.getNames().toArray(String[]::new));
  }
}
