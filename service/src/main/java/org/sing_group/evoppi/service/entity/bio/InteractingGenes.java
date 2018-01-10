package org.sing_group.evoppi.service.entity.bio;

import static java.util.Objects.requireNonNull;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;

public class InteractingGenes {
  private final Gene geneA;
  private final Gene geneB;
  
  public InteractingGenes(Interaction interaction) {
    this(interaction.getGeneA(), interaction.getGeneB());
  }
  
  public InteractingGenes(Gene geneA, Gene geneB) {
    requireNonNull(geneA, "geneA can't be null");
    requireNonNull(geneB, "geneB can't be null");
    
    if (geneA.compareTo(geneB) < 0) {
      this.geneA = geneA;
      this.geneB = geneB;
    } else {
      this.geneA = geneB;
      this.geneB = geneA;
    }
  }

  public Gene getGeneA() {
    return geneA;
  }
  
  public Gene getGeneB() {
    return geneB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
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
    return true;
  }

  @Override
  public String toString() {
    return "InteractingGenes [geneA=" + geneA.getId() + ", geneB=" + geneB.getId() + "]";
  }
}
