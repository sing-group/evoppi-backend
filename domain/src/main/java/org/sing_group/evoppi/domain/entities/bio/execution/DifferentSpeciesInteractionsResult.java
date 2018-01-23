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

import org.sing_group.evoppi.domain.dao.bio.execution.BlastQueryOptions;

@Entity
@DiscriminatorValue("DIFF")
@Table(name = "different_species_interactions_result")
public class DifferentSpeciesInteractionsResult extends InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "referenceInteractomeId", nullable = false)
  private int referenceInteractomeId;

  @Column(name = "targetInteractomeId", nullable = false)
  private int targetInteractomeId;

  @Embedded
  private BlastQueryOptions blastQueryOptions;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
    name = "interactionsResultId", referencedColumnName = "id", foreignKey = @ForeignKey(
      name = "FK_different_species_interactions_result_blast_result"
    )
  )
  private Set<BlastResult> blastResults;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "different_species_interactions_result_reference_genes",
    joinColumns = @JoinColumn(name = "interactionsResultId", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_reference_genes")
  )
  @Column(name = "geneId", nullable = false)
  private Set<Integer> referenceGeneIds;
  
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "different_species_interactions_result_target_genes",
    joinColumns = @JoinColumn(name = "interactionsResultId", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "FK_different_species_interactions_result_target_genes")
  )
  @Column(name = "geneId", nullable = false)
  private Set<Integer> targetGeneIds;

  DifferentSpeciesInteractionsResult() {}

  public DifferentSpeciesInteractionsResult(
    int queryGeneId, int referenceInteractomeId, int targetInteractomeId, BlastQueryOptions blastQueryOptions,
    int queryMaxDegree
  ) {
    super(queryGeneId, queryMaxDegree);
    this.referenceInteractomeId = referenceInteractomeId;
    this.targetInteractomeId = targetInteractomeId;

    this.blastQueryOptions = blastQueryOptions;
    this.blastResults = new HashSet<>();
  }

  public int getReferenceInteractomeId() {
    return referenceInteractomeId;
  }

  public int getTargetInteractomeId() {
    return targetInteractomeId;
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
  
  public void setReferenceGeneIds(Collection<Integer> geneIds) {
    this.referenceGeneIds = new HashSet<>(geneIds);
  }
  
  public Stream<Integer> getReferenceGeneIds() {
    return referenceGeneIds.stream();
  }
  
  public void setTargetGeneIds(Collection<Integer> geneIds) {
    this.targetGeneIds = new HashSet<>(geneIds);
  }
  
  public Stream<Integer> getTargetGeneIds() {
    return targetGeneIds.stream();
  }
}
