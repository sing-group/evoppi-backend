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

package org.sing_group.evoppi.domain.dao.spi.bio.execution;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.user.User;

public interface SameSpeciesInteractionsResultDAO {

  public boolean exists(String interactionResultId);

  public SameSpeciesInteractionsResult get(String interactionResultId);

  public SameSpeciesInteractionsResult create(
    String name, String description, String resultReference,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes, User owner
  );

  public SameSpeciesInteractionsResult create(
    String name, String description, Function<String, String> resultReferenceBuilder,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes, User owner
  );

  public Stream<SameSpeciesInteractionsResult> listById(
    String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  );

  public Stream<SameSpeciesInteractionsResult> listUserResults(
    User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  );

  public long coungById(String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions);
  
  public long countByUser(User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions);
}
