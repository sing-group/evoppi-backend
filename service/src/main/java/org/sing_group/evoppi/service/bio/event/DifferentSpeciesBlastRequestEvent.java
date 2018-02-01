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
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;

public class DifferentSpeciesBlastRequestEvent extends DifferentSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private final Path referenceFastaPath;
  private final Path targetFastaPath;
  private final Collection<GeneInteraction> referenceInteractions;

  public DifferentSpeciesBlastRequestEvent(
    DifferentSpeciesCalculusEvent event, Path referenceFastaPath, Path targetFastaPath, Collection<GeneInteraction> referenceInteractions
  ) {
    super(event);

    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    this.referenceInteractions = new HashSet<>(referenceInteractions);
  }

  public DifferentSpeciesBlastRequestEvent(
    int geneId, int referenceInteractome, int targetInteractome, BlastQueryOptions blastQueryOptions, int maxDegree,
    int workId, int resultId, Path referenceFastaPath, Path targetFastaPath, Collection<GeneInteraction> referenceInteractions
  ) {
    super(geneId, referenceInteractome, targetInteractome, blastQueryOptions, maxDegree, workId, resultId);

    this.referenceFastaPath = referenceFastaPath;
    this.targetFastaPath = targetFastaPath;
    this.referenceInteractions = new HashSet<>(referenceInteractions);
  }
  
  public Path getReferenceFastaPath() {
    return referenceFastaPath;
  }
  
  public Path getTargetFastaPath() {
    return targetFastaPath;
  }

  public Stream<GeneInteraction> getReferenceInteractions() {
    return referenceInteractions.stream();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((referenceFastaPath == null) ? 0 : referenceFastaPath.hashCode());
    result = prime * result + ((referenceInteractions == null) ? 0 : referenceInteractions.hashCode());
    result = prime * result + ((targetFastaPath == null) ? 0 : targetFastaPath.hashCode());
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
    DifferentSpeciesBlastRequestEvent other = (DifferentSpeciesBlastRequestEvent) obj;
    if (referenceFastaPath == null) {
      if (other.referenceFastaPath != null)
        return false;
    } else if (!referenceFastaPath.equals(other.referenceFastaPath))
      return false;
    if (referenceInteractions == null) {
      if (other.referenceInteractions != null)
        return false;
    } else if (!referenceInteractions.equals(other.referenceInteractions))
      return false;
    if (targetFastaPath == null) {
      if (other.targetFastaPath != null)
        return false;
    } else if (!targetFastaPath.equals(other.targetFastaPath))
      return false;
    return true;
  }
}
