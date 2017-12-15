package org.sing_group.evoppi.service.entity.bio;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

public class InteractionGroup {
  private final Gene geneFrom;
  private final Gene geneTo;
  private final Set<Interaction> interactions;

  public InteractionGroup(Collection<Interaction> interactions) {
    if (!doInteractionsHaveSameGenes(interactions)) {
      throw new IllegalArgumentException("Interactions do not have same genes");
    }
    
    final Interaction firstInteraction = interactions.iterator().next();
    this.geneFrom = firstInteraction.getGeneFrom();
    this.geneTo = firstInteraction.getGeneTo();
    this.interactions = new HashSet<>(interactions);
  }

  private static boolean doInteractionsHaveSameGenes(Collection<Interaction> interaction) {
    final long geneFromCount = interaction.stream()
      .map(Interaction::getGeneFrom)
      .distinct()
    .count();
    
    if (geneFromCount != 1) {
      return false;
    }
    
    final long geneToCount = interaction.stream()
      .map(Interaction::getGeneTo)
      .distinct()
    .count();
    
    return geneToCount == 1;
  }
  
  public Gene getGeneTo() {
    return geneTo;
  }
  
  public Gene getGeneFrom() {
    return geneFrom;
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
    result = prime * result + ((geneFrom == null) ? 0 : geneFrom.hashCode());
    result = prime * result + ((geneTo == null) ? 0 : geneTo.hashCode());
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
    if (geneFrom == null) {
      if (other.geneFrom != null)
        return false;
    } else if (!geneFrom.equals(other.geneFrom))
      return false;
    if (geneTo == null) {
      if (other.geneTo != null)
        return false;
    } else if (!geneTo.equals(other.geneTo))
      return false;
    if (interactions == null) {
      if (other.interactions != null)
        return false;
    } else if (!interactions.equals(other.interactions))
      return false;
    return true;
  }
}
