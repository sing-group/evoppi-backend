/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome.GeneInInteractomeId;

@Entity
@Table(name = "gene_in_interactome")
@IdClass(GeneInInteractomeId.class)
public class GeneInInteractome implements Serializable {
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
  @JoinColumn(name = "gene", referencedColumnName = "id", nullable = false)
  private Gene gene;

  public Species getSpecies() {
    return species;
  }

  public Interactome getInteractome() {
    return interactome;
  }

  public Gene getGene() {
    return gene;
  }

  public static class GeneInInteractomeId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int species;
    private int interactome;
    private int gene;
    
    GeneInInteractomeId() {}

    public GeneInInteractomeId(int species, int interactome, int gene) {
      this.species = species;
      this.interactome = interactome;
      this.gene = gene;
    }

    public int getSpecies() {
      return species;
    }

    public void setSpecies(int species) {
      this.species = species;
    }

    public int getInteractome() {
      return interactome;
    }

    public void setInteractome(int interactome) {
      this.interactome = interactome;
    }

    public int getGene() {
      return gene;
    }

    public void setGene(int gene) {
      this.gene = gene;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + gene;
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
      GeneInInteractomeId other = (GeneInInteractomeId) obj;
      if (gene != other.gene)
        return false;
      if (interactome != other.interactome)
        return false;
      if (species != other.species)
        return false;
      return true;
    }
  }
}
