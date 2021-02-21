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

package org.sing_group.evoppi.rest.resource.spi.bio;

import javax.ws.rs.core.Response;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResultListingField;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultField;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResultListingField;

public interface InteractionResource {
  public Response getInteractions(
    int geneId, int[] interactomes, int[] referenceInteractomes, int[] targetInteractomes, int maxDegree,
    double evalue, int maxTargetSeqs, double minimumIdentity, int minimumAlignmentLength
  );

  public Response listDifferentSpeciesResults(
    String ids, Integer start, Integer end, DifferentSpeciesInteractionsResultListingField order, SortDirection sort
  );

  public Response listSameSpeciesResults(
    String ids, Integer start, Integer end, SameSpeciesInteractionsResultListingField order, SortDirection sort
  );

  public Response getInterationResult(
    String resultId,
    Integer page,
    Integer pageSize,
    InteractionGroupResultField orderField,
    SortDirection sortDirection,
    Integer interactomeId,
    boolean summarize
  );

  public Response getInterationResultInteractomeSingleFasta(String resultId, boolean includeVersionSuffix);

  public Response getInterationResultInteractomeFasta(String resultId, int interactomeId, boolean includeVersionSuffix);

  public Response getInterationResultInteractions(
    String id,
    Integer page,
    Integer pageSize,
    InteractionGroupResultField orderField,
    SortDirection sortDirection,
    Integer interactomeId
  );

  public Response deleteInterationResult(String id);
}
