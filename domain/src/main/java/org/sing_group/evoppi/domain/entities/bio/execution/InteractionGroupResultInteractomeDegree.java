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
package org.sing_group.evoppi.domain.entities.bio.execution;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResultInteractomeDegree.InteractionGroupResultInteractomeDegreeId;

@Entity
@Table(name = "interaction_group_result_interactome_degree")
@IdClass(InteractionGroupResultInteractomeDegreeId.class)
public class InteractionGroupResultInteractomeDegree {

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
  @JoinColumn(name = "interactionsResult", referencedColumnName = "id", nullable = false)
  private InteractionsResult interactionsResult;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
  @JoinColumn(name = "geneA", referencedColumnName = "id", nullable = false)
  private Gene geneA;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
  @JoinColumn(name = "geneB", referencedColumnName = "id", nullable = false)
  private Gene geneB;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
  @JoinColumn(name = "interactome", referencedColumnName = "id", nullable = false)
  private Interactome interactome;
  
  @Column(name = "degree", nullable = false)
  private int degree;
  
  InteractionGroupResultInteractomeDegree() {}
    
  public InteractionGroupResultInteractomeDegree(
    InteractionsResult interactionsResult,
    Gene geneA,
    Gene geneB,
    Interactome interactome,
    int degree
  ) {
    this.interactionsResult = interactionsResult;
    this.geneA = geneA;
    this.geneB = geneB;
    this.interactome = interactome;
    this.degree = degree;
  }
  
  public Interactome getInteractome() {
    return interactome;
  }
  
  public int getDegree() {
    return degree;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
    result = prime * result + ((interactionsResult == null) ? 0 : interactionsResult.hashCode());
    result = prime * result + ((interactome == null) ? 0 : interactome.hashCode());
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
    InteractionGroupResultInteractomeDegree other = (InteractionGroupResultInteractomeDegree) obj;
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
    if (interactionsResult == null) {
      if (other.interactionsResult != null)
        return false;
    } else if (!interactionsResult.equals(other.interactionsResult))
      return false;
    if (interactome == null) {
      if (other.interactome != null)
        return false;
    } else if (!interactome.equals(other.interactome))
      return false;
    return true;
  }



  public static class InteractionGroupResultInteractomeDegreeId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String interactionsResult;
    private int geneA;
    private int geneB;
    private int interactome;

    InteractionGroupResultInteractomeDegreeId() {}

    public InteractionGroupResultInteractomeDegreeId(InteractionsResult interactionsResult, Gene geneA, Gene geneB, Interactome interactome) {
      this(interactionsResult.getId(), geneA.getId(), geneB.getId(), interactome.getId());
    }

    public InteractionGroupResultInteractomeDegreeId(String interactionsResult, int geneA, int geneB, int interactome) {
      this.interactionsResult = interactionsResult;
      this.geneA = geneA;
      this.geneB = geneB;
      this.interactome = interactome;
    }

    public String getInteractionsResult() {
      return interactionsResult;
    }

    public void setInteractionsResult(String interactionsResult) {
      this.interactionsResult = interactionsResult;
    }

    public int getGeneA() {
      return geneA;
    }

    public void setGeneA(int geneAId) {
      this.geneA = geneAId;
    }

    public int getGeneB() {
      return geneB;
    }

    public void setGeneB(int geneBId) {
      this.geneB = geneBId;
    }
    
    public int getInteractome() {
      return interactome;
    }

    public void setInteractome(int interactome) {
      this.interactome = interactome;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneA;
      result = prime * result + geneB;
      result = prime * result + ((interactionsResult == null) ? 0 : interactionsResult.hashCode());
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
      InteractionGroupResultInteractomeDegreeId other = (InteractionGroupResultInteractomeDegreeId) obj;
      if (geneA != other.geneA)
        return false;
      if (geneB != other.geneB)
        return false;
      if (interactionsResult == null) {
        if (other.interactionsResult != null)
          return false;
      } else if (!interactionsResult.equals(other.interactionsResult))
        return false;
      if (interactome != other.interactome)
        return false;
      return true;
    }
  }
}
