package org.sing_group.evoppi.service.entity.bio;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;

public class InteractingGenes {
  private final Gene geneFrom;
  private final Gene geneTo;
  
  public InteractingGenes(Interaction interaction) {
    this(interaction.getGeneFrom(), interaction.getGeneTo());
  }
  
  public InteractingGenes(Gene geneFrom, Gene geneTo) {
    this.geneFrom = geneFrom;
    this.geneTo = geneTo;
  }

  public Gene getGeneFrom() {
    return geneFrom;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneFrom == null) ? 0 : geneFrom.hashCode());
    result = prime * result + ((geneTo == null) ? 0 : geneTo.hashCode());
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
    InteractingGenes other = (InteractingGenes) obj;
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
    return true;
  }

}
