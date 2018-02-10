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
package org.sing_group.evoppi.rest.resource.spi.bio;

import javax.ws.rs.core.Response;

public interface InteractionResource {
  public Response getInteractions(
    int geneId, int[] interactomes, Integer referenceInteractome, Integer targetInteractome, int maxDegree,
    double evalue, int maxTargetSeqs, double minimumIdentity, int minimumAlignmentLength
  );

  public Response getInterationResult(String resultId);

  public Response getInterationResultInteractomeSingleFasta(String resultId, boolean includeVersionSuffix);
  
  public Response getInterationResultInteractomeFasta(String resultId, int interactomeId, boolean includeVersionSuffix);
}
