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

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData.InteractingGenes;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.InteractomeMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

@Default
@Transactional(MANDATORY)
public class DefaultInteractomeMapper implements InteractomeMapper {
  private UriBuilder uriBuilder;

  @PersistenceContext
  private EntityManager em;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
  }

  @Override
  public InteractomeData toInteractomeData(Interactome interactome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    return new InteractomeData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpeciesA().getId(),
        pathBuilder.species(interactome.getSpeciesA()).build()
      ),
      new IdAndUri(
        interactome.getSpeciesB().getId(),
        pathBuilder.species(interactome.getSpeciesB()).build()
      ),
      interactome.getInteractomeType()
    );
  }

  @Override
  public InteractomeWithInteractionsData toInteractomeWithInteractionsData(Interactome interactome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    final IdAndUri[] genes =
      interactome.getInteractions()
        .flatMap(Interaction::getGenes)
        .distinct()
        .map(gene -> new IdAndUri(gene.getId(), pathBuilder.gene(gene).build()))
        .toArray(IdAndUri[]::new);

    final InteractingGenes[] interactions =
      interactome.getInteractions()
        .map(interaction -> new InteractingGenes(interaction.getGeneAId(), interaction.getGeneBId()))
        .toArray(InteractingGenes[]::new);

    return new InteractomeWithInteractionsData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpeciesA().getId(),
        pathBuilder.species(interactome.getSpeciesA()).build()
      ),
      new IdAndUri(
        interactome.getSpeciesB().getId(),
        pathBuilder.species(interactome.getSpeciesB()).build()
      ),
      interactome.getInteractomeType(),
      genes,
      interactions
    );
  }

  @Override
  public String toInteractomeTsv(Interactome interactome) {
    StringBuilder sb = new StringBuilder();

    interactome.getInteractions().forEach(i -> {
      sb
        .append(i.getGeneA().getId())
        .append("\t")
        .append(i.getGeneB().getId())
        .append("\n");
    });

    return sb.toString();
  }
}
