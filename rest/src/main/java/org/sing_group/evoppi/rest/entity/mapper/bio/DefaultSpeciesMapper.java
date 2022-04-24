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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.bio.InteractomeType;
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.SpeciesMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.service.spi.storage.GeneStorageService;

@Default
@Transactional(MANDATORY)
public class DefaultSpeciesMapper implements SpeciesMapper {
  private UriBuilder uriBuilder;

  @PersistenceContext
  private EntityManager em;

  @Inject
  private GeneStorageService geneStorageService;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
  }

  @Override
  public SpeciesData toSpeciesData(Species species) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    IdAndUri[] databaseInteractomes =
      species.getInteractomes().filter(i -> i.getInteractomeType().equals(InteractomeType.DATABASE)).map(
        interactome -> new IdAndUri(
          interactome.getId(),
          pathBuilder.interactome((DatabaseInteractome) interactome).build()
        )
      )
        .toArray(IdAndUri[]::new);

    IdAndUri[] predictomes =
      species.getInteractomes().filter(i -> i.getInteractomeType().equals(InteractomeType.PREDICTOME)).map(
        interactome -> new IdAndUri(
          interactome.getId(),
          pathBuilder.predictome((Predictome) interactome).build()
        )
      )
        .toArray(IdAndUri[]::new);
    
    return new SpeciesData(
      species.getId(),
      species.getName(),
      databaseInteractomes,
      predictomes
    );
  }

  @Override
  public File toSpeciesFasta(Species species) {
    Path fasta;
    try {
      fasta = this.geneStorageService.createFasta(species);

      return fasta.toFile();
    } catch (IOException e) {
      throw new RuntimeException("Error writing FASTA file.");
    }
  }
}
