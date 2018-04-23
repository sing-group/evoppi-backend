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
import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult.InteractionGroupResultId;

@Entity
@Table(name = "interaction_group_result")
@IdClass(InteractionGroupResultId.class)
public class InteractionGroupResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "interactionsResultId")
  private String interactionsResultId;

  @Id
  @Column(name = "geneAId")
  private int geneAId;

  @Id
  @Column(name = "geneBId")
  private int geneBId;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "interaction_group_result_interactome",
    joinColumns = {
      @JoinColumn(name = "interactionsResultId", referencedColumnName = "interactionsResultId"),
      @JoinColumn(name = "geneAId", referencedColumnName = "geneAId"),
      @JoinColumn(name = "geneBId", referencedColumnName = "geneBId")
    },
    foreignKey = @ForeignKey(name = "FK_interaction_group_result_interactome")
  )
  @Column(name = "degree", nullable = false)
  private Map<Integer, Integer> interactomeDegrees;

  InteractionGroupResult() {}
  
  public InteractionGroupResult(
    String interactionsResultId, int geneAId, int geneBId, Map<Integer, Integer> interactomeDegrees
  ) {
    this.interactionsResultId = interactionsResultId;
    this.geneAId = geneAId;
    this.geneBId = geneBId;
    this.interactomeDegrees = new HashMap<>(interactomeDegrees);
  }
  
  public InteractionGroupResult(
    String interactionsResultId, int geneAId, int geneBId, int interactome, int degree
  ) {
    this(interactionsResultId, geneAId, geneBId, singletonMap(interactome, degree));
  }

  public String getInteractionsResultId() {
    return interactionsResultId;
  }

  public int getGeneAId() {
    return geneAId;
  }

  public int getGeneBId() {
    return geneBId;
  }
  
  public IntStream getGeneIds() {
    return IntStream.of(this.getGeneAId(), this.getGeneBId());
  }

  public IntStream getInteractomeIds() {
    return interactomeDegrees.keySet().stream().mapToInt(Integer::intValue);
  }
  
  public Map<Integer, Integer> getInteractomeDegrees() {
    return unmodifiableMap(interactomeDegrees);
  }
  
  public boolean belongsToInteractome(int interactomeId) {
    return this.interactomeDegrees.containsKey(interactomeId);
  }

  public void addInteractome(int interactomeId, int degree) {
    this.interactomeDegrees.put(interactomeId, degree);
  }

  public void addInteractomes(Map<Integer, Integer> interactomeDegrees) {
    for (Map.Entry<Integer, Integer> entry : interactomeDegrees.entrySet()) {
      this.interactomeDegrees.putIfAbsent(entry.getKey(), entry.getValue());
//      if (this.interactomeDegrees.containsKey(entry.getKey())) {
//        final Integer currentDegree = this.interactomeDegrees.get(entry.getKey());
//        
//        if (currentDegree != entry.getValue()) {
//          throw new IllegalArgumentException(String.format(
//            "Interactome %d already present with degree %d instead of %d",
//            entry.getKey(), currentDegree, entry.getValue()
//          ));
//        }
//      } else {
//        this.interactomeDegrees.put(entry.getKey(), entry.getValue());
//      }
    }
  }

  public static class InteractionGroupResultId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String interactionsResultId;

    private int geneAId;

    private int geneBId;

    InteractionGroupResultId() {}

    public InteractionGroupResultId(String interactionsResultId, int geneAId, int geneBId) {
      this.interactionsResultId = interactionsResultId;
      this.geneAId = geneAId;
      this.geneBId = geneBId;
    }

    public String getInteractionsResultId() {
      return interactionsResultId;
    }

    public void setInteractionsResultId(String interactionsResultId) {
      this.interactionsResultId = interactionsResultId;
    }

    public int getGeneAId() {
      return geneAId;
    }

    public void setGeneAId(int geneAId) {
      this.geneAId = geneAId;
    }

    public int getGeneBId() {
      return geneBId;
    }

    public void setGeneBId(int geneBId) {
      this.geneBId = geneBId;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneAId;
      result = prime * result + geneBId;
      result = prime * result + ((interactionsResultId == null) ? 0 : interactionsResultId.hashCode());
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
      if (geneAId != other.geneAId)
        return false;
      if (geneBId != other.geneBId)
        return false;
      if (interactionsResultId == null) {
        if (other.interactionsResultId != null)
          return false;
      } else if (!interactionsResultId.equals(other.interactionsResultId))
        return false;
      return true;
    }
  }
}
