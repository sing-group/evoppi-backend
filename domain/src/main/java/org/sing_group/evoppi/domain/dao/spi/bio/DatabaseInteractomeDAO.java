/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.dao.spi.bio;

import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

public interface DatabaseInteractomeDAO {

  public Stream<DatabaseInteractome> list(ListingOptions<DatabaseInteractome> listingOptions);

  public DatabaseInteractome get(int id);

  public long count(ListingOptions<DatabaseInteractome> listingOptions);

  /*
   * TODO: check this method to add speciesA and speciesB
   */
  public DatabaseInteractome create(
    String name, String dbSourceIdType, Integer numOriginalInteractions, Integer numUniqueOriginalInteractions,
    Integer numUniqueOriginalGenes, Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    Species species, GeneInteractions interactions
  );
}
