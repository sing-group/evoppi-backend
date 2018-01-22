/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Interaction.InteractionId;

@Entity
@Table(name = "interaction")
@IdClass(InteractionId.class)
public class Interaction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "interactome", referencedColumnName = "id", nullable = false)
  private Interactome interactome;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "geneA", referencedColumnName = "id", nullable = false)
  private Gene geneA;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "geneB", referencedColumnName = "id", nullable = false)
  private Gene geneB;

  public Interactome getInteractome() {
    return interactome;
  }

  public Gene getGeneA() {
    return geneA;
  }

  public Gene getGeneB() {
    return geneB;
  }
  
  public Stream<Gene> getGenes() {
    return Stream.of(this.geneA, this.geneB);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + getGeneA().getId();
    result = prime * result + getGeneB().getId();
    result = prime * result + getInteractome().getId();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!getClass().isAssignableFrom(obj.getClass()))
      return false;
    Interaction other = (Interaction) obj;
    if (getGeneA().getId() != other.getGeneA().getId())
      return false;
    if (getGeneB().getId() != other.getGeneB().getId())
      return false;
    if (!getInteractome().getId().equals(other.getInteractome().getId()))
      return false;
    return true;
  }

  public static class InteractionId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int interactome;
    private int geneA;
    private int geneB;
    
    InteractionId() {}
    
    public InteractionId(int interactome, int geneA, int geneB) {
      this.interactome = interactome;
      this.geneA = geneA;
      this.geneB = geneB;
    }

    public int getInteractome() {
      return interactome;
    }

    public int getGeneA() {
      return geneA;
    }

    public int getGeneB() {
      return geneB;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneA;
      result = prime * result + geneB;
      result = prime * result + interactome;
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
      InteractionId other = (InteractionId) obj;
      if (geneA != other.geneA)
        return false;
      if (geneB != other.geneB)
        return false;
      if (interactome != other.interactome)
        return false;
      return true;
    }
  }

  @Override
  public String toString() {
    return "Interaction [interactome=" + interactome.getId() + ", geneA=" + geneA.getId() + ", geneB=" + geneB.getId() + "]";
  }
}
