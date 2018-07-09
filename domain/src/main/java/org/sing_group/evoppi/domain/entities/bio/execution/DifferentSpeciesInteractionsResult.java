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

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

@Entity
@DiscriminatorValue("DIFF")
@Table(name = "different_species_interactions_result")
public class DifferentSpeciesInteractionsResult extends InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {})
  @JoinTable(
    name = "different_species_interactions_result_reference_interactomes",
    joinColumns = {
      @JoinColumn(name = "resultId", referencedColumnName = "id")
    },
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_reference_interactomes")
  )
  private Set<Interactome> referenceInteractome;
  
  @ManyToMany(fetch = FetchType.LAZY, cascade = {})
  @JoinTable(
    name = "different_species_interactions_result_target_interactomes",
    joinColumns = {
      @JoinColumn(name = "resultId", referencedColumnName = "id")
    },
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_target_interactomes")
  )
  private Set<Interactome> targetInteractome;

  @Embedded
  private BlastQueryOptions blastQueryOptions;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
    name = "interactionsResultId",
    referencedColumnName = "id",
    foreignKey = @ForeignKey(
      name = "FK_different_species_interactions_result_blast_result"
    )
  )
  private Set<BlastResult> blastResults;

  DifferentSpeciesInteractionsResult() {}

  public DifferentSpeciesInteractionsResult(
    String name,
    String description,
    String resultReference,
    Gene queryGene,
    Collection<Interactome> referenceInteractome,
    Collection<Interactome> targetInteractome,
    BlastQueryOptions blastQueryOptions,
    int queryMaxDegree
  ) {
    super(name, description, resultReference, queryGene, queryMaxDegree);
    
    this.referenceInteractome = new HashSet<>(referenceInteractome);
    this.targetInteractome = new HashSet<>(targetInteractome);

    this.blastQueryOptions = blastQueryOptions;
    this.blastResults = new HashSet<>();
  }
  
  public DifferentSpeciesInteractionsResult(
    String name,
    String description,
    Function<String, String> resultReferenceBuilder,
    Gene queryGene,
    Collection<Interactome> referenceInteractome,
    Collection<Interactome> targetInteractome,
    BlastQueryOptions blastQueryOptions,
    int queryMaxDegree
  ) {
    super(name, description, resultReferenceBuilder, queryGene, queryMaxDegree);
    
    this.referenceInteractome = new HashSet<>(referenceInteractome);
    this.targetInteractome = new HashSet<>(targetInteractome);
    
    this.blastQueryOptions = blastQueryOptions;
    this.blastResults = new HashSet<>();
  }

  public IntStream getReferenceInteractomeIds() {
    return getReferenceInteractomes().mapToInt(Interactome::getId);
  }
  
  public Stream<Interactome> getReferenceInteractomes() {
    return this.referenceInteractome.stream();
  }

  public IntStream getTargetInteractomeIds() {
    return getTargetInteractomes().mapToInt(Interactome::getId);
  }
  
  public Stream<Interactome> getTargetInteractomes() {
    return this.targetInteractome.stream();
  }
  
  public BlastQueryOptions getBlastQueryOptions() {
    return blastQueryOptions;
  }

  public Stream<BlastResult> getBlastResults() {
    return this.blastResults.stream();
  }

  public boolean addBlastResult(BlastResult blastResult) {
    requireNonNull(blastResult, "blastResult should not be null");
    
    return this.blastResults.add(blastResult);
  }

  public boolean hasBlastResults() {
    return !this.blastResults.isEmpty();
  }
  
  public boolean hasReferenceInteractions() {
    return this.getReferenceInteractions().count() > 0;
  }
  
  public boolean hasTargetInteractions() {
    return this.getTargetInteractions().count() > 0;
  }
  
  public Stream<InteractionGroupResult> getReferenceInteractions() {
    final Predicate<InteractionGroupResult> belongsToReference =
      interaction -> this.getReferenceInteractomeIds()
        .anyMatch(interactomeId -> interaction.belongsToInteractome(interactomeId));
    
    return this.getInteractions().filter(belongsToReference);
  }
  
  public Stream<InteractionGroupResult> getTargetInteractions() {
    final Predicate<InteractionGroupResult> belongsToTarget =
      interaction -> this.getTargetInteractomeIds()
        .anyMatch(interactomeId -> interaction.belongsToInteractome(interactomeId));
      
    return this.getInteractions().filter(belongsToTarget);
  }
  
  public IntStream getReferenceGeneIds() {
    return this.getReferenceInteractions()
      .flatMapToInt(InteractionGroupResult::getGeneIds)
      .distinct();
  }
  
  public IntStream getTargetGeneIds() {
    return this.getTargetInteractions()
      .flatMapToInt(InteractionGroupResult::getGeneIds)
      .distinct();
  }
  
  public boolean hasReferenceGene(int geneId) {
    return this.getReferenceGeneIds()
      .anyMatch(referenceGeneId -> referenceGeneId == geneId);
  }
  
  public boolean hasTargetGene(int geneId) {
    return this.getTargetGeneIds()
      .anyMatch(targetGeneId -> targetGeneId == geneId);
  }
  
  public IntStream getInteractomeIds() {
    return IntStream.concat(this.getReferenceInteractomeIds(), this.getTargetInteractomeIds());
  }

  public boolean hasInteractome(int interactomeId) {
    return this.getInteractomeIds()
      .anyMatch(id -> interactomeId == id);
  }
}
