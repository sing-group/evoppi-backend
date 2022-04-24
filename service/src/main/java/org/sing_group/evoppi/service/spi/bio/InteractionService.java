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
package org.sing_group.evoppi.service.spi.bio;

import java.util.function.Function;
import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultListingOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.user.User;
import org.sing_group.evoppi.service.user.entity.InteractionResultLinkage;

@Local
public interface InteractionService {
  public SameSpeciesInteractionsResult findSameSpeciesInteractions(
    int geneId, int[] interactomes, int maxDegree, Function<String, String> resultReferenceBuilder
  );

  public DifferentSpeciesInteractionsResult findDifferentSpeciesInteractions(
    int geneId, int[] referenceInteractomes, int[] targetInteractomes, BlastQueryOptions blastOptions, int maxDegree,
    Function<String, String> resultReferenceBuilder
  );

  public SameSpeciesInteractionsResult getSameSpeciesResult(String id);

  public DifferentSpeciesInteractionsResult getDifferentSpeciesResult(String id);

  public Stream<SameSpeciesInteractionsResult> listSameSpeciesResult(
    String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  );

  public Stream<SameSpeciesInteractionsResult> listUserSameSpeciesResult(
    User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  );

  public Stream<DifferentSpeciesInteractionsResult> listDifferentSpeciesResult(
    String[] ids, ListingOptions<DifferentSpeciesInteractionsResult> listingOptions
  );

  public Stream<DifferentSpeciesInteractionsResult> listUserDifferentSpeciesResult(
    User user, ListingOptions<DifferentSpeciesInteractionsResult> listingOptions
  );

  public boolean isSameSpeciesResult(String id);

  public boolean isDifferentSpeciesResult(String id);

  public String getSameSpeciesResultFasta(String resultId, int interactomeId, boolean includeVersionSuffix);

  public String getDifferentSpeciesResultFasta(String resultId, int interactomeId, boolean includeVersionSuffix);

  public String getSameSpeciesResultSingleFasta(String resultId, boolean includeVersionSuffix);

  public String getDifferentSpeciesResultSingleFasta(String resultId, boolean includeVersionSuffix);

  public Stream<InteractionGroupResult> getInteractions(
    InteractionsResult result,
    InteractionGroupResultListingOptions filteringOptions
  );

  public void deleteResult(String id);

  public InteractionResultLinkage linkDifferentSpeciesResultsToCurrentUser(String[] uuids);

  public InteractionResultLinkage linkSameSpeciesResultsToCurrentUser(String[] uuids);

  public long count();

  public long countSameSpeciesResult(String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions);
  
  public long countDifferentSpeciesResult(String[] ids, ListingOptions<DifferentSpeciesInteractionsResult> listingOptions);

  public long countSameByUser(User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions);

  public long countDifferentByUser(User user, ListingOptions<DifferentSpeciesInteractionsResult> listingOptions);
}
