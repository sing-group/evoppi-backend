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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.evoppi.domain.entities.execution.WorkEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", length = 4)
@Table(name = "interactions_result")
public abstract class InteractionsResult extends WorkEntity {
  private static final long serialVersionUID = 1L;
  
  @Column(name = "queryGeneId", nullable = false)
  private int queryGeneId;
  
  @Column(name = "queryMaxDegree", nullable = false)
  private int queryMaxDegree;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
    name = "interactionsResultId", referencedColumnName = "id", foreignKey = @ForeignKey(
      name = "FK_interactions_result_interaction_group_result"
    )
  )
  private Set<InteractionGroupResult> interactions;
  
  @Transient
  private Map<Integer, Map<Integer, InteractionGroupResult>> interactionsIndex;

  protected InteractionsResult() {
    super();
    this.interactions = new HashSet<>();
    this.interactionsIndex = new HashMap<>();
  }
  
  protected InteractionsResult(String name, int queryGeneId, int queryMaxDegree) {
    this(name, null, (String) null, queryGeneId, queryMaxDegree);
  }
  
  protected InteractionsResult(String name, String description, String resultReference, int queryGeneId, int queryMaxDegree) {
    super(name, description, resultReference);
    
    this.queryGeneId = queryGeneId;
    this.queryMaxDegree = queryMaxDegree;
  }
  
  protected InteractionsResult(String name, String description, Function<String, String> resultReferenceBuilder, int queryGeneId, int queryMaxDegree) {
    super(name, description, resultReferenceBuilder);
    
    this.queryGeneId = queryGeneId;
    this.queryMaxDegree = queryMaxDegree;
  }

  public int getQueryGeneId() {
    return queryGeneId;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public Stream<InteractionGroupResult> getInteractions() {
    return interactions.stream();
  }
  
  @PostLoad
  private void createGroupIndex() {
    for (InteractionGroupResult result : this.interactions) {
      this.interactionsIndex.compute(result.getGeneAId(), createRemappingFunction(result));
    }
  }
  
  private static BiFunction<Integer, Map<Integer, InteractionGroupResult>, Map<Integer, InteractionGroupResult>> createRemappingFunction(
    InteractionGroupResult result
  ) {
    return (geneA, geneBMap) -> {
      if (geneBMap == null) geneBMap = new HashMap<>();
      
      geneBMap.put(result.getGeneBId(), result);
      
      return geneBMap;
    };
  }
  
  private Optional<InteractionGroupResult> getInteraction(int geneAId, int geneBId) {
    final Map<Integer, InteractionGroupResult> geneBMap = this.interactionsIndex.get(geneAId);
    
    return geneBMap == null ? Optional.empty() : Optional.ofNullable(geneBMap.get(geneBId));
  }
  
  public InteractionGroupResult addInteraction(int geneAId, int geneBId, Map<Integer, Integer> interactomeDegrees) {
    final Optional<InteractionGroupResult> maybeAGroup = this.getInteraction(geneAId, geneBId);
    
    if (maybeAGroup.isPresent()) {
      final InteractionGroupResult group = maybeAGroup.get();

      group.addInteractomes(interactomeDegrees);
      
      return group;
    } else {
      final InteractionGroupResult newResult = new InteractionGroupResult(this.getId(), geneAId, geneBId, interactomeDegrees);
      
      this.interactions.add(newResult);
      this.interactionsIndex.compute(geneAId, createRemappingFunction(newResult));
      
      return newResult;
    }
  }

  public InteractionGroupResult addInteraction(int geneA, int geneB, int interactomeId) {
    return this.addInteraction(geneA, geneB, interactomeId, -1);
  }

  public InteractionGroupResult addInteraction(int geneAId, int geneBId, int interactomeId, int degree) {
    final Optional<InteractionGroupResult> maybeAGroup = this.getInteraction(geneAId, geneBId);
    
    if (maybeAGroup.isPresent()) {
      final InteractionGroupResult group = maybeAGroup.get();

      group.addInteractome(interactomeId, degree);
      
      return group;
    } else {
      final InteractionGroupResult newResult = new InteractionGroupResult(this.getId(), geneAId, geneBId, interactomeId, degree);
      
      this.interactions.add(newResult);
      this.interactionsIndex.compute(geneAId, createRemappingFunction(newResult));
      
      return newResult;
    }
  }
  
  public boolean hasInteractionsForInteractome(int interactomeId) {
    return this.interactions.stream()
      .anyMatch(interaction -> interaction.belongsToInteractome(interactomeId));
  }
}
