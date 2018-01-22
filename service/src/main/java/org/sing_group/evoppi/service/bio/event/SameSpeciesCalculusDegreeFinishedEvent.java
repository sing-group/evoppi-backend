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

import static java.util.stream.Collectors.joining;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class SameSpeciesCalculusDegreeFinishedEvent
extends SameSpeciesCalculusEvent
implements Serializable, WorkStepEvent {
  private static final long serialVersionUID = 1L;

  private final int currentDegree;
  private final Set<GeneInteraction> interactions;

  public SameSpeciesCalculusDegreeFinishedEvent(
    SameSpeciesCalculusEvent event, int currentDegree, Set<GeneInteraction> interactions
  ) {
    super(event);
    this.currentDegree = currentDegree;
    this.interactions = interactions;
  }

  public SameSpeciesCalculusDegreeFinishedEvent(
    int geneId, int[] interactomes, int maxDegree, int workId, int resultId, int currentDegree,
    Set<GeneInteraction> interactions
  ) {
    super(geneId, interactomes, maxDegree, workId, resultId);

    this.currentDegree = currentDegree;
    this.interactions = new HashSet<>(interactions);
  }

  public int getCurrentDegree() {
    return currentDegree;
  }

  public Stream<GeneInteraction> getInteractions() {
    return interactions.stream();
  }

  @Override
  public String getDescription() {
    return String.format("%d interactions found in degree %d for gene %d in interactomes: %s",
      this.getInteractions().count(),
      this.getCurrentDegree(),
      this.getGeneId(),
      IntStream.of(this.getInteractomes()).mapToObj(Integer::toString).collect(joining(", "))
    );
  }

  @Override
  public double getProgress() {
    return this.getCurrentDegree() / ((double) this.getMaxDegree() + 1);
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return ExecutionStatus.RUNNING;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + currentDegree;
    result = prime * result + ((interactions == null) ? 0 : interactions.hashCode());
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
    SameSpeciesCalculusDegreeFinishedEvent other = (SameSpeciesCalculusDegreeFinishedEvent) obj;
    if (currentDegree != other.currentDegree)
      return false;
    if (interactions == null) {
      if (other.interactions != null)
        return false;
    } else if (!interactions.equals(other.interactions))
      return false;
    return true;
  }

  public static class GeneInteraction implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int geneAId;
    private final int geneBId;
    private final int[] interactomeIds;

    public GeneInteraction(int geneAId, int geneBId, int[] interactomeIds) {
      this.geneAId = geneAId;
      this.geneBId = geneBId;
      this.interactomeIds = interactomeIds;
    }

    public int getGeneAId() {
      return geneAId;
    }

    public int getGeneBId() {
      return geneBId;
    }

    public IntStream getInteractomeIds() {
      return IntStream.of(interactomeIds);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneAId;
      result = prime * result + geneBId;
      result = prime * result + Arrays.hashCode(interactomeIds);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      GeneInteraction other = (GeneInteraction) obj;
      if (geneAId != other.geneAId)
        return false;
      if (geneBId != other.geneBId)
        return false;
      if (!Arrays.equals(interactomeIds, other.interactomeIds))
        return false;
      return true;
    }
  }
}