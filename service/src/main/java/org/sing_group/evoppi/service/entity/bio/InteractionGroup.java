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
package org.sing_group.evoppi.service.entity.bio;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

public class InteractionGroup {
  private final InteractingGenes interactingGenes;
  private final Set<Interaction> interactions;
  
  private final int degree;

  public InteractionGroup(Collection<Interaction> interactions, int degree) {
    if (!doInteractionsHaveSameGenes(interactions)) {
      throw new IllegalArgumentException("Interactions do not have same genes");
    }
    
    final Interaction firstInteraction = interactions.iterator().next();
    this.interactingGenes = new InteractingGenes(firstInteraction);
    this.interactions = new HashSet<>(interactions);
    
    this.degree = degree;
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
  
  public InteractingGenes getInteractingGenes() {
    return interactingGenes;
  }
  
  public Gene getGeneA() {
    return this.interactingGenes.getGeneA();
  }
  
  public Gene getGeneB() {
    return this.interactingGenes.getGeneB();
  }

  public Stream<Interaction> getInteractions() {
    return interactions.stream();
  }
  
  public Stream<Interactome> getInteractomes() {
    return this.getInteractions()
      .map(Interaction::getInteractome)
    .distinct();
  }
  
  public int getDegree() {
    return degree;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + degree;
    result = prime * result + ((interactingGenes == null) ? 0 : interactingGenes.hashCode());
    result = prime * result + ((interactions == null) ? 0 : interactions.hashCode());
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
    if (degree != other.degree)
      return false;
    if (interactingGenes == null) {
      if (other.interactingGenes != null)
        return false;
    } else if (!interactingGenes.equals(other.interactingGenes))
      return false;
    if (interactions == null) {
      if (other.interactions != null)
        return false;
    } else if (!interactions.equals(other.interactions))
      return false;
    return true;
  }

  @Override
  public String toString() {
    final String interactomes = this.getInteractions()
      .map(Interaction::getInteractome)
      .map(Interactome::getId)
      .map(Object::toString)
    .collect(Collectors.joining(","));
    
    return "InteractionGroup [genes=" + this.interactingGenes + ", interactions=" + interactomes + ", degree=" + degree + "]";
  }
}
