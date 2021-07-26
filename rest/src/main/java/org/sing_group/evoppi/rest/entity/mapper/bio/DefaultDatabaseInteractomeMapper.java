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

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.MANDATORY;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.rest.entity.IdAndUri;
import org.sing_group.evoppi.rest.entity.bio.DatabaseInteractomeData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.DatabaseInteractomeMapper;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

@Default
@Transactional(MANDATORY)
public class DefaultDatabaseInteractomeMapper implements DatabaseInteractomeMapper {
  private UriBuilder uriBuilder;

  @PersistenceContext
  private EntityManager em;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
  }

  @Override
  public DatabaseInteractomeData toDatabaseInteractomeData(DatabaseInteractome dbInteractome) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);

    return new DatabaseInteractomeData(
      dbInteractome.getId(),
      dbInteractome.getName(),
      new IdAndUri(
        dbInteractome.getSpeciesA().getId(),
        pathBuilder.species(dbInteractome.getSpeciesA()).build()
      ),
      new IdAndUri(
        dbInteractome.getSpeciesB().getId(),
        pathBuilder.species(dbInteractome.getSpeciesB()).build()
      ),
      dbInteractome.getDbSourceIdType().orElse(null),
      dbInteractome.getNumOriginalInteractions().isPresent() ? dbInteractome.getNumOriginalInteractions().getAsInt()
        : null,
      dbInteractome.getNumUniqueOriginalInteractions().isPresent()
        ? dbInteractome.getNumUniqueOriginalInteractions().getAsInt()
        : null,
      dbInteractome.getNumUniqueOriginalGenes().isPresent() ? dbInteractome.getNumUniqueOriginalGenes().getAsInt()
        : null,
      dbInteractome.getNumInteractionsNotToUniProtKB().isPresent()
        ? dbInteractome.getNumInteractionsNotToUniProtKB().getAsInt()
        : null,
      dbInteractome.getNumGenesNotToUniProtKB().isPresent() ? dbInteractome.getNumGenesNotToUniProtKB().getAsInt()
        : null,
      dbInteractome.getNumInteractionsNotToGeneId().isPresent()
        ? dbInteractome.getNumInteractionsNotToGeneId().getAsInt()
        : null,
      dbInteractome.getNumGenesNotToGeneId().isPresent() ? dbInteractome.getNumGenesNotToGeneId().getAsInt() : null,
      dbInteractome.getNumFinalInteractions().isPresent() ? dbInteractome.getNumFinalInteractions().getAsInt() : null,
      dbInteractome.getNumFinalGenes().isPresent() ? dbInteractome.getNumFinalGenes().getAsInt() : null,
      dbInteractome.getNumRemovedInterSpeciesInteractions().isPresent()
        ? dbInteractome.getNumRemovedInterSpeciesInteractions().getAsInt()
        : null,
      dbInteractome.getNumMultimappedToGeneId().isPresent() ? dbInteractome.getNumMultimappedToGeneId().getAsInt()
        : null
    );
  }
}
