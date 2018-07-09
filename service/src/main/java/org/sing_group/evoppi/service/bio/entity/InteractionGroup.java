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

package org.sing_group.evoppi.service.bio.entity;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;

public class InteractionGroup implements HasGenePair {
  private final HasGenePair interactingGenes;
  private final Map<Interaction, Integer> interactions;

  public InteractionGroup(Map<Interaction, Integer> interactions) {
    if (!doInteractionsHaveSameGenes(interactions.keySet())) {
      throw new IllegalArgumentException("Interactions do not have same genes");
    }
    
    final Interaction firstInteraction = interactions.keySet().iterator().next();
    this.interactingGenes = firstInteraction;
    this.interactions = new HashMap<>(interactions);
  }

  private static boolean doInteractionsHaveSameGenes(Collection<Interaction> interactions) {
    final ToLongFunction<Function<Interaction, Gene>> countGenes =
      getter -> interactions.stream()
        .map(getter)
        .distinct()
      .count();
    
    return countGenes.applyAsLong(Interaction::getGeneA) == 1
      && countGenes.applyAsLong(Interaction::getGeneB) == 1;
  }
  
  @Override
  public Gene getGeneA() {
    return this.interactingGenes.getGeneA();
  }
  
  @Override
  public Gene getGeneB() {
    return this.interactingGenes.getGeneB();
  }

  public Stream<Interaction> getInteractions() {
    return interactions.keySet().stream();
  }
  
  public boolean hasInteraction(Interaction interaction) {
    return this.interactions.containsKey(interaction);
  }
  
  public Map<Interaction, Integer> getInteractionDegrees() {
    return unmodifiableMap(this.interactions);
  }
  
  public int getDegree(Interaction interaction) {
    return this.interactions.get(interaction);
  }
  
  public void addInteraction(Interaction interaction) {
    this.addInteraction(interaction, -1);
  }
  
  private boolean isValidInteraction(Interaction interaction) {
    return this.interactingGenes.getGeneA().equals(interaction.getGeneA())
      && this.interactingGenes.getGeneB().equals(interaction.getGeneB());
  }
  
  public void addInteraction(Interaction interaction, int degree) {
    if (this.hasInteractome(interaction.getInteractome())) {
      throw new IllegalArgumentException("Interactome already present");
    } else if (!this.isValidInteraction(interaction)) {
      throw new IllegalArgumentException("Invalid interaction. Genes do not match with this group");
    } else {
      this.interactions.put(interaction, degree);
    }
  }
  
  private boolean hasInteractome(Interactome interactome) {
    return this.interactions.keySet().stream()
      .map(Interaction::getInteractome)
      .anyMatch(interactome::equals);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((interactingGenes == null) ? 0 : interactingGenes.hashCode());
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
    InteractionGroup other = (InteractionGroup) obj;
    if (interactingGenes == null) {
      if (other.interactingGenes != null)
        return false;
    } else if (!interactingGenes.equals(other.interactingGenes))
      return false;
    return true;
  }

  @Override
  public String toString() {
    final String interactomes = this.getInteractions()
      .map(Interaction::getInteractome)
      .map(Interactome::getId)
      .map(Object::toString)
    .collect(joining(","));
    
    return "InteractionGroup [genes=" + this.interactingGenes + ", interactions=" + interactomes + "]";
  }
}
