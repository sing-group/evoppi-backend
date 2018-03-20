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
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("DIFF")
@Table(name = "different_species_interactions_result")
public class DifferentSpeciesInteractionsResult extends InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @ElementCollection
  @CollectionTable(
    name = "different_species_interactions_result_reference_interactomes",
    joinColumns = {
      @JoinColumn(name = "resultId", referencedColumnName = "id")
    },
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_reference_interactomes")
  )
  @Column(name = "interactomeId", nullable = false)
  private Set<Integer> referenceInteractomeIds;

  @ElementCollection
  @CollectionTable(
    name = "different_species_interactions_result_target_interactomes",
    joinColumns = {
      @JoinColumn(name = "resultId", referencedColumnName = "id")
    },
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_target_interactomes")
  )
  @Column(name = "interactomeId", nullable = false)
  private Set<Integer> targetInteractomeIds;

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
    int queryGeneId,
    Set<Integer> referenceInteractomeIds,
    Set<Integer> targetInteractomeIds,
    BlastQueryOptions blastQueryOptions,
    int queryMaxDegree
  ) {
    super(queryGeneId, queryMaxDegree);
    
    this.referenceInteractomeIds = new HashSet<>(referenceInteractomeIds);
    this.targetInteractomeIds = new HashSet<>(targetInteractomeIds);

    this.blastQueryOptions = blastQueryOptions;
    this.blastResults = new HashSet<>();
  }

  public IntStream getReferenceInteractomeIds() {
    return referenceInteractomeIds.stream().mapToInt(Integer::intValue);
  }

  public IntStream getTargetInteractomeIds() {
    return targetInteractomeIds.stream().mapToInt(Integer::intValue);
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
}
