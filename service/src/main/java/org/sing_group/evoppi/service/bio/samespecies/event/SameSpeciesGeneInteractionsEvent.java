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
package org.sing_group.evoppi.service.bio.samespecies.event;

import static java.util.stream.Collectors.joining;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class SameSpeciesGeneInteractionsEvent
extends SameSpeciesCalculusEvent
implements Serializable, WorkStepEvent {
  private static final long serialVersionUID = 1L;
  
  private final Set<GeneInteraction> interactions;

  public SameSpeciesGeneInteractionsEvent(SameSpeciesCalculusEvent event, Collection<GeneInteraction> interactions) {
    super(event);
    this.interactions = new HashSet<>(interactions);
  }

  public SameSpeciesGeneInteractionsEvent(int geneId, int[] interactomes, int maxDegree, String workId, String resultId,
    Collection<GeneInteraction> interactions
  ) {
    super(geneId, interactomes, maxDegree, workId, resultId);
    this.interactions = new HashSet<>(interactions);
  }

  public Stream<GeneInteraction> getInteractions() {
    return interactions.stream();
  }
  
  @Override
  public String getDescription() {
    return String.format("%d interactions found for gene %d in interactomes: %s",
      this.getInteractions().count(),
      this.getGeneId(),
      this.getInteractomes().mapToObj(Integer::toString).collect(joining(", "))
    );
  }

  @Override
  public double getProgress() {
    return 0.5d;
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return ExecutionStatus.RUNNING;
  }
}
