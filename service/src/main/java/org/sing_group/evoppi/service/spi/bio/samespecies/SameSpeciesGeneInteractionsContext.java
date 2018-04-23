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
package org.sing_group.evoppi.service.spi.bio.samespecies;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.InteractionIds;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineContext;

public interface SameSpeciesGeneInteractionsContext
extends PipelineContext<
  SameSpeciesGeneInteractionsConfiguration,
  SameSpeciesGeneInteractionsContext,
  SameSpeciesGeneInteractionsStep,
  SameSpeciesGeneInteractionsPipeline,
  SameSpeciesGeneInteractionsEvent,
  SameSpeciesGeneInteractionsEventManager
> {
  public IntStream getInteractionsDegrees();
  
  public Stream<InteractionIds> getInteractionsWithDegree(int degree);
  
  public Stream<InteractionIds> getCompletedInteractions();
  
  public default Map<Integer, Set<InteractionIds>> getInteractionsByDegree() {
    return this.getInteractionsDegrees()
      .boxed()
      .collect(toMap(
        identity(),
        degree -> this.getInteractionsWithDegree(degree).collect(toSet())
      ));
  }
  
  public default boolean hasInteractions() {
    return this.getInteractionsDegrees().count() > 0;
  }
  
  public boolean hasCompletedInteractions();
  
  public default boolean hasInteractionsWithDegree(int degree) {
    return this.getInteractionsDegrees().anyMatch(d -> d == degree);
  }
  
  public default Stream<InteractionIds> getInteractions() {
    return this.getInteractionsByDegree().values().stream()
      .flatMap(Set::stream);
  }
}
