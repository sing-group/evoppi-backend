/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.mapper.bio;

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.function.IntPredicate;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData.InteractingGenes;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.GeneMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

@Default
@Transactional(MANDATORY)
public class DefaultGeneMapper implements GeneMapper {
  private UriBuilder uriBuilder;

  @PersistenceContext
  private EntityManager em;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
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

  @Override
  public GeneNamesData toGeneNamesData(Gene gene, IntPredicate interactomeFilter) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    final GeneNameData[] geneNames =
      gene.getNames()
        .map(this::toGeneNameData)
        .toArray(GeneNameData[]::new);

    final IdAndUri[] interactomes =
      gene.getInteractions()
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
}
