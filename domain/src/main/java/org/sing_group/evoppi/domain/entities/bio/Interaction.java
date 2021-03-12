/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Interaction.InteractionId;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteraction;

@Entity
@Table(name = "interaction")
@IdClass(InteractionId.class)
public class Interaction implements HasGeneInteraction, Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "species", referencedColumnName = "id", nullable = false)
  private Species species;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "interactome", referencedColumnName = "id", nullable = false)
  private Interactome interactome;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "geneA", referencedColumnName = "id", nullable = false)
  private Gene geneA;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "geneB", referencedColumnName = "id", nullable = false)
  private Gene geneB;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns({
    @JoinColumn(name = "species", referencedColumnName = "species", nullable = false, insertable = false, updatable = false),
    @JoinColumn(name = "interactome", referencedColumnName = "interactome", nullable = false, insertable = false, updatable = false),
    @JoinColumn(name = "geneA", referencedColumnName = "gene", nullable = false, insertable = false, updatable = false)
  })
  private GeneInInteractome geneInInteractomeA;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns({
    @JoinColumn(name = "species", referencedColumnName = "species", nullable = false, insertable = false, updatable = false),
    @JoinColumn(name = "interactome", referencedColumnName = "interactome", nullable = false, insertable = false, updatable = false),
    @JoinColumn(name = "geneB", referencedColumnName = "gene", nullable = false, insertable = false, updatable = false)
  })
  private GeneInInteractome geneInInteractomeB;

  Interaction() {}

  public Interaction(Species species, Interactome interactome, Gene geneA, Gene geneB) {
    this.species = species;
    this.interactome = interactome;
    this.geneA = geneA;
    this.geneB = geneB;
    this.geneInInteractomeA = new GeneInInteractome(species, interactome, geneA);
    this.geneInInteractomeB = new GeneInInteractome(species, interactome, geneB);
  }

  @Override
  public Interactome getInteractome() {
    return interactome;
  }

  @Override
  public Gene getGeneA() {
    return geneA;
  }
  
  @Override
  public Gene getGeneB() {
    return geneB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
    result = prime * result + ((interactome == null) ? 0 : interactome.hashCode());
    result = prime * result + ((species == null) ? 0 : species.hashCode());
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
    Interaction other = (Interaction) obj;
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
    if (interactome == null) {
      if (other.interactome != null)
        return false;
    } else if (!interactome.equals(other.interactome))
      return false;
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }

  public static class InteractionId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int species;
    private int interactome;
    private int geneA;
    private int geneB;
    
    InteractionId() {}
    
    public InteractionId(int species, int interactome, int geneA, int geneB) {
      this.species = species;
      this.interactome = interactome;
      this.geneA = geneA;
      this.geneB = geneB;
    }
    
    public int getSpecies() {
      return species;
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
      result = prime * result + species;
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
      if (species != other.species)
        return false;
      return true;
    }
  }
}
