/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.bio.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;

public class DifferentSpeciesCalculateTargetInteractionsRequestEvent extends DifferentSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private final Collection<GeneInteraction> referenceInteractions;
  private final Collection<BlastResult> blastResults;

  public DifferentSpeciesCalculateTargetInteractionsRequestEvent(
    DifferentSpeciesCalculusEvent event, Collection<GeneInteraction> referenceInteractions, Collection<BlastResult> blastResults
  ) {
    super(event);

    this.referenceInteractions = new HashSet<>(referenceInteractions);
    this.blastResults = new HashSet<>(blastResults);
  }

  public DifferentSpeciesCalculateTargetInteractionsRequestEvent(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastQueryOptions, int maxDegree,
    int workId, int resultId, Collection<GeneInteraction> referenceInteractions, Collection<BlastResult> blastResults
  ) {
    super(geneId, referenceInteractome, targetInteractome, blastQueryOptions, maxDegree, workId, resultId);

    this.referenceInteractions = new HashSet<>(referenceInteractions);
    this.blastResults = new HashSet<>(blastResults);
  }
  
  public Stream<GeneInteraction> getReferenceInteractions() {
    return referenceInteractions.stream();
  }

  public Stream<BlastResult> getBlastResults() {
    return blastResults.stream();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((blastResults == null) ? 0 : blastResults.hashCode());
    result = prime * result + ((referenceInteractions == null) ? 0 : referenceInteractions.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    DifferentSpeciesCalculateTargetInteractionsRequestEvent other = (DifferentSpeciesCalculateTargetInteractionsRequestEvent) obj;
    if (blastResults == null) {
      if (other.blastResults != null)
        return false;
    } else if (!blastResults.equals(other.blastResults))
      return false;
    if (referenceInteractions == null) {
      if (other.referenceInteractions != null)
        return false;
    } else if (!referenceInteractions.equals(other.referenceInteractions))
      return false;
    return true;
  }
}
