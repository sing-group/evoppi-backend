/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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

import java.util.Optional;
import java.util.OptionalInt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "interactome_database")
public class DatabaseInteractome extends Interactome {
  private static final long serialVersionUID = 1L;

  @Column(name = "dbSourceIdType", length = 100, nullable = true)
  private String dbSourceIdType;

  @Column(name = "numOriginalInteractions", nullable = true)
  private Integer numOriginalInteractions;

  @Column(name = "numUniqueOriginalInteractions", nullable = true)
  private Integer numUniqueOriginalInteractions;

  @Column(name = "numUniqueOriginalGenes", nullable = true)
  private Integer numUniqueOriginalGenes;

  @Column(name = "numInteractionsNotToUniProtKB", nullable = true)
  private Integer numInteractionsNotToUniProtKB;

  @Column(name = "numGenesNotToUniProtKB", nullable = true)
  private Integer numGenesNotToUniProtKB;

  @Column(name = "numInteractionsNotToGeneId", nullable = true)
  private Integer numInteractionsNotToGeneId;

  @Column(name = "numGenesNotToGeneId", nullable = true)
  private Integer numGenesNotToGeneId;

  @Column(name = "numFinalInteractions", nullable = true)
  private Integer numFinalInteractions;

  @Column(name = "numFinalGenes", nullable = true)
  private Integer numFinalGenes;

  @Column(name = "numRemovedInterSpeciesInteractions", nullable = true)
  private Integer numRemovedInterSpeciesInteractions;

  @Column(name = "numMultimappedToGeneId", nullable = true)
  private Integer numMultimappedToGeneId;

  DatabaseInteractome() {}
  
  public DatabaseInteractome(
    String name, String dbSourceIdType, Integer numOriginalInteractions, Integer numUniqueOriginalInteractions,
    Integer numUniqueOriginalGenes, Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    Species speciesA, Species speciesB, InteractomeCollection collection
  ) {
    super(name, speciesA, speciesB, collection);

    this.dbSourceIdType = dbSourceIdType;
    this.numOriginalInteractions = numOriginalInteractions;
    this.numUniqueOriginalInteractions = numUniqueOriginalInteractions;
    this.numUniqueOriginalGenes = numUniqueOriginalGenes;
    this.numInteractionsNotToUniProtKB = numInteractionsNotToUniProtKB;
    this.numGenesNotToUniProtKB = numGenesNotToUniProtKB;
    this.numInteractionsNotToGeneId = numInteractionsNotToGeneId;
    this.numGenesNotToGeneId = numGenesNotToGeneId;
    this.numFinalInteractions = numFinalInteractions;
    this.numFinalGenes = numFinalGenes;
    this.numRemovedInterSpeciesInteractions = numRemovedInterSpeciesInteractions;
    this.numMultimappedToGeneId = numMultimappedToGeneId;
  }
  
  public Optional<String> getDbSourceIdType() {
    return Optional.ofNullable(dbSourceIdType);
  }

  public OptionalInt getNumOriginalInteractions() {
    return numOriginalInteractions == null ? OptionalInt.empty() : OptionalInt.of(this.numOriginalInteractions);
  }

  public OptionalInt getNumUniqueOriginalInteractions() {
    return numUniqueOriginalInteractions == null ? OptionalInt.empty()
      : OptionalInt.of(this.numUniqueOriginalInteractions);
  }

  public OptionalInt getNumUniqueOriginalGenes() {
    return numUniqueOriginalGenes == null ? OptionalInt.empty() : OptionalInt.of(this.numUniqueOriginalGenes);
  }

  public OptionalInt getNumInteractionsNotToUniProtKB() {
    return numInteractionsNotToUniProtKB == null ? OptionalInt.empty()
      : OptionalInt.of(this.numInteractionsNotToUniProtKB);
  }

  public OptionalInt getNumGenesNotToUniProtKB() {
    return numGenesNotToUniProtKB == null ? OptionalInt.empty() : OptionalInt.of(this.numGenesNotToUniProtKB);
  }

  public OptionalInt getNumInteractionsNotToGeneId() {
    return numInteractionsNotToGeneId == null ? OptionalInt.empty() : OptionalInt.of(this.numInteractionsNotToGeneId);
  }

  public OptionalInt getNumGenesNotToGeneId() {
    return numGenesNotToGeneId == null ? OptionalInt.empty() : OptionalInt.of(this.numGenesNotToGeneId);
  }

  public OptionalInt getNumFinalInteractions() {
    return numFinalInteractions == null ? OptionalInt.empty() : OptionalInt.of(this.numFinalInteractions);
  }

  public OptionalInt getNumFinalGenes() {
    return numFinalGenes == null ? OptionalInt.empty() : OptionalInt.of(this.numFinalGenes);
  }

  public OptionalInt getNumRemovedInterSpeciesInteractions() {
    return numRemovedInterSpeciesInteractions == null ? OptionalInt.empty()
      : OptionalInt.of(this.numRemovedInterSpeciesInteractions);
  }

  public OptionalInt getNumMultimappedToGeneId() {
    return numMultimappedToGeneId == null ? OptionalInt.empty() : OptionalInt.of(this.numMultimappedToGeneId);
  }
  
  @Override
  public InteractomeType getInteractomeType() {
    return InteractomeType.DATABASE;
  }
}
