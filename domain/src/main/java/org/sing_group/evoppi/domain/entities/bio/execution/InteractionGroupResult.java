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

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult.InteractionGroupResultId;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;

@Entity
@Table(name = "interaction_group_result")
@IdClass(InteractionGroupResultId.class)
public class InteractionGroupResult implements HasGenePair, Serializable {
  private static final long serialVersionUID = 1L;

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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumns(
    value = {
      @JoinColumn(name = "interactionsResult", referencedColumnName = "interactionsResult"),
      @JoinColumn(name = "geneA", referencedColumnName = "geneA"),
      @JoinColumn(name = "geneB", referencedColumnName = "geneB"),
    },
    foreignKey = @ForeignKey(name = "FK_interaction_group_result_interactome_degree")
  )
  private Set<InteractionGroupResultInteractomeDegree> interactomeDegrees;

  InteractionGroupResult() {}
  
  public InteractionGroupResult(
    InteractionsResult interactionsResult, HasGenePair genePair, Map<Interactome, Integer> interactomeDegrees
  ) {
    this.interactionsResult = interactionsResult;
    this.geneA = genePair.getGeneA();
    this.geneB = genePair.getGeneB();
    this.interactomeDegrees = interactomeDegrees.entrySet().stream()
      .map(entry -> new InteractionGroupResultInteractomeDegree(
        InteractionGroupResult.this.interactionsResult,
        InteractionGroupResult.this.geneA,
        InteractionGroupResult.this.geneB,
        entry.getKey(),
        entry.getValue()
      ))
    .collect(toSet());
  }
  
  public InteractionGroupResult(
    InteractionsResult interactionsResult, HasGenePair genePair, Interactome interactome, int degree
  ) {
    this(interactionsResult, genePair, singletonMap(interactome, degree));
  }

  public InteractionsResult getInteractionsResult() {
    return interactionsResult;
  }

  @Override
  public Gene getGeneA() {
    return geneA;
  }
  
  @Override
  public Gene getGeneB() {
    return geneB;
  }
  
  public IntStream getInteractomeIds() {
    return this.getInteractomes()
      .mapToInt(Interactome::getId);
  }
  
  public Stream<Interactome> getInteractomes() {
    return this.interactomeDegrees.stream()
      .map(InteractionGroupResultInteractomeDegree::getInteractome);
  }
  
  public Map<Interactome, Integer> getInteractomeDegrees() {
    return this.interactomeDegrees.stream()
      .collect(toMap(
        InteractionGroupResultInteractomeDegree::getInteractome,
        InteractionGroupResultInteractomeDegree::getDegree
      ));
  }
  
  public Map<Integer, Integer> getInteractomeDegreesById() {
    return this.interactomeDegrees.stream()
      .collect(toMap(
        interactomeDegree -> interactomeDegree.getInteractome().getId(),
        InteractionGroupResultInteractomeDegree::getDegree
      ));
  }
  
  public int getDegreeForInteractome(int interactomeId) {
    final Optional<Interactome> interactome = this.getInteractomes()
      .filter(candidateInteractome -> candidateInteractome.getId() == interactomeId)
    .findAny();
    
    return interactome.map(this::getDegreeForInteractome)
      .orElseThrow(() -> new IllegalArgumentException("Invalid interactome id: " + interactomeId));
  }

  public int getDegreeForInteractome(Interactome interactome) {
    return this.getDegreeForInteractome(interactome.getId());
  }
  
  public boolean belongsToInteractome(int interactomeId) {
    return this.getInteractomes()
      .anyMatch(candidateInteractome -> candidateInteractome.getId() == interactomeId);
  }
  
  public boolean belongsToInteractome(Interactome interactome) {
    return this.getInteractomes()
      .anyMatch(candidateInteractome -> candidateInteractome.equals(interactome));
  }

  public void addInteractome(Interactome interactome, int degree) {
    this.interactomeDegrees.add(new InteractionGroupResultInteractomeDegree(
      this.interactionsResult,
      this.geneA,
      this.geneB,
      interactome,
      degree
    ));
  }

  public void addInteractomes(Map<Interactome, Integer> interactomeDegrees) {
    interactomeDegrees.entrySet().stream()
      .filter(entry -> !this.belongsToInteractome(entry.getKey()))
    .forEach(entry -> this.addInteractome(entry.getKey(), entry.getValue()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
    result = prime * result + ((interactionsResult == null) ? 0 : interactionsResult.hashCode());
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
    InteractionGroupResult other = (InteractionGroupResult) obj;
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
    return true;
  }

  public static class InteractionGroupResultId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String interactionsResult;
    private int geneA;
    private int geneB;

    InteractionGroupResultId() {}

    public InteractionGroupResultId(InteractionsResult interactionsResult, Gene geneA, Gene geneB) {
      this(interactionsResult.getId(), geneA.getId(), geneB.getId());
    }

    public InteractionGroupResultId(String interactionsResult, int geneA, int geneB) {
      this.interactionsResult = interactionsResult;
      this.geneA = geneA;
      this.geneB = geneB;
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

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneA;
      result = prime * result + geneB;
      result = prime * result + ((interactionsResult == null) ? 0 : interactionsResult.hashCode());
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
      InteractionGroupResultId other = (InteractionGroupResultId) obj;
      if (geneA != other.geneA)
        return false;
      if (geneB != other.geneB)
        return false;
      if (interactionsResult == null) {
        if (other.interactionsResult != null)
          return false;
      } else if (!interactionsResult.equals(other.interactionsResult))
        return false;
      return true;
    }
  }
}
