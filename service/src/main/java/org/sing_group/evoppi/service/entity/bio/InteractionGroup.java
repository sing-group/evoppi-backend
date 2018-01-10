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
  private final Gene geneA;
  private final Gene geneB;
  private final Set<Interaction> interactions;

  public InteractionGroup(Collection<Interaction> interactions) {
    if (!doInteractionsHaveSameGenes(interactions)) {
      throw new IllegalArgumentException("Interactions do not have same genes");
    }
    
    final Interaction firstInteraction = interactions.iterator().next();
    this.geneA = firstInteraction.getGeneA();
    this.geneB = firstInteraction.getGeneB();
    this.interactions = new HashSet<>(interactions);
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
  
  public Gene getGeneB() {
    return geneB;
  }
  
  public Gene getGeneA() {
    return geneA;
  }

  public Stream<Interaction> getInteractions() {
    return interactions.stream();
  }
  
  public Stream<Interactome> getInteractomes() {
    return this.getInteractions()
      .map(Interaction::getInteractome)
    .distinct();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
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
    if (geneA == null) {
      if (other.geneA != null)
        return false;
    } else if (!geneA.equals(other.geneA))
      return false;
    if (geneB == null) {
      if (other.geneB != null)
        return false;
    } else if (!geneB.equals(other.geneB))
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
    final String interactomes = interactions.stream()
      .map(Interaction::getInteractome)
      .map(Interactome::getId)
      .map(Object::toString)
    .collect(Collectors.joining(","));
    
    return "InteractionGroup [geneA=" + geneA.getId() + ", geneB=" + geneB.getId() + ", interactions=" + interactomes + "]";
  }
}
