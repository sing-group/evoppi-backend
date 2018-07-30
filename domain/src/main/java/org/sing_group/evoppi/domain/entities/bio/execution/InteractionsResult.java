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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePairIds;
import org.sing_group.evoppi.domain.entities.user.User;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", length = 4)
@Table(name = "interactions_result")
public abstract class InteractionsResult extends WorkEntity {
  private static final long serialVersionUID = 1L;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "queryGene", referencedColumnName = "id", nullable = false,
    foreignKey = @ForeignKey(
      name = "FK_gene_interactions_result"
    )
  )
  private Gene queryGene;
  
  @Column(name = "queryMaxDegree", nullable = false)
  private int queryMaxDegree;

  // Required due to a bug that fails lazy-loading fields in super-classes
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "interactionsResult", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private Set<InteractionGroupResult> interactions;
  
  @Transient
  private Map<Integer, Map<Integer, InteractionGroupResult>> interactionsIndex;

  protected InteractionsResult() {
    super();
    this.interactions = new HashSet<>();
  }
  
  protected InteractionsResult(String name, Gene queryGene, int queryMaxDegree) {
    this(name, null, (String) null, queryGene, queryMaxDegree, null);
  }
  
  protected InteractionsResult(String name, String description, String resultReference, Gene queryGene, int queryMaxDegree, User owner) {
    super(name, description, resultReference, owner);
    
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
  }
  
  protected InteractionsResult(String name, String description, Function<String, String> resultReferenceBuilder, Gene queryGene, int queryMaxDegree, User owner) {
    super(name, description, resultReferenceBuilder, owner);
    
    this.queryGene = queryGene;
    this.queryMaxDegree = queryMaxDegree;
  }
  
  public void setOwner(User owner) {
    if (this.owner != null)
      throw new IllegalStateException("Result already has an owner");
    
    owner.addResult(this);
    this.owner = owner;
  }

  public int getQueryGeneId() {
    return this.queryGene.getId();
  }
  
  public Gene getQueryGene() {
    return queryGene;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public Stream<InteractionGroupResult> getInteractions() {
    return interactions.stream();
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
  
  private Optional<InteractionGroupResult> getInteraction(HasGenePairIds genePairIds) {
    final Map<Integer, InteractionGroupResult> geneBMap = this.getInteractionsIndex().get(genePairIds.getGeneAId());
    
    return geneBMap == null ? Optional.empty() : Optional.ofNullable(geneBMap.get(genePairIds.getGeneBId()));
  }
  
  public InteractionGroupResult addInteraction(HasGenePair genePair, Map<Interactome, Integer> interactomeDegrees) {
    final Optional<InteractionGroupResult> maybeAGroup = this.getInteraction(genePair);
    
    if (maybeAGroup.isPresent()) {
      final InteractionGroupResult group = maybeAGroup.get();

      group.addInteractomes(interactomeDegrees);
      
      return group;
    } else {
      final InteractionGroupResult newResult = new InteractionGroupResult(this, genePair, interactomeDegrees);
      
      this.interactions.add(newResult);
      this.getInteractionsIndex().compute(genePair.getGeneAId(), createRemappingFunction(newResult));
      
      return newResult;
    }
  }

  public InteractionGroupResult addInteraction(HasGenePair genePair, Interactome interactome) {
    return this.addInteraction(genePair, interactome, -1);
  }

  public InteractionGroupResult addInteraction(HasGenePair genePair, Interactome interactome, int degree) {
    final Optional<InteractionGroupResult> maybeAGroup = this.getInteraction(genePair);
    
    if (maybeAGroup.isPresent()) {
      final InteractionGroupResult group = maybeAGroup.get();

      group.addInteractome(interactome, degree);
      
      return group;
    } else {
      final InteractionGroupResult newResult = new InteractionGroupResult(this, genePair, interactome, degree);
      
      this.interactions.add(newResult);
      this.getInteractionsIndex().compute(genePair.getGeneAId(), createRemappingFunction(newResult));
      
      return newResult;
    }
  }
  
  public boolean hasInteractionsForInteractome(int interactomeId) {
    return this.interactions.stream()
      .anyMatch(interaction -> interaction.belongsToInteractome(interactomeId));
  }
  
  private Map<Integer, Map<Integer, InteractionGroupResult>> getInteractionsIndex() {
    if (this.interactionsIndex == null) {
      synchronized(this) {
        if (this.interactionsIndex == null) {
          this.interactionsIndex = new HashMap<>();
          
          for (InteractionGroupResult result : this.interactions) {
            this.interactionsIndex.compute(result.getGeneAId(), createRemappingFunction(result));
          }
        }
      } 
    }
    
    return this.interactionsIndex;
  }
}
