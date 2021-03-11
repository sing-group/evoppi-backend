/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePairIds;

@Entity
@Table(
  name = "interactome",
  uniqueConstraints = @UniqueConstraint(columnNames = {"name", "species"}),
  indexes = @Index(columnList = "id, species")
)
public class Interactome implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "species", referencedColumnName = "id", nullable = false)
  private Species species;
  
  @OneToMany(mappedBy = "interactome", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactions;
  
  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Species getSpecies() {
    return species;
  }

  public Optional<String> getDbSourceIdType() {
    return Optional.ofNullable(dbSourceIdType);
  }

  public OptionalInt getNumOriginalInteractions() {
    return numOriginalInteractions == null ? OptionalInt.empty() : OptionalInt.of(this.numOriginalInteractions);
  }

  public OptionalInt getNumUniqueOriginalInteractions() {
    return numUniqueOriginalInteractions == null ? OptionalInt.empty() : OptionalInt.of(this.numUniqueOriginalInteractions);
  }

  public OptionalInt getNumUniqueOriginalGenes() {
    return numUniqueOriginalGenes == null ? OptionalInt.empty() : OptionalInt.of(this.numUniqueOriginalGenes);
  }

  public OptionalInt getNumInteractionsNotToUniProtKB() {
    return numInteractionsNotToUniProtKB == null ? OptionalInt.empty() : OptionalInt.of(this.numInteractionsNotToUniProtKB);
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
    return numRemovedInterSpeciesInteractions == null ? OptionalInt.empty() : OptionalInt.of(this.numRemovedInterSpeciesInteractions);
  }
  
  public OptionalInt getNumMultimappedToGeneId() {
    return numMultimappedToGeneId == null ? OptionalInt.empty() : OptionalInt.of(this.numMultimappedToGeneId);
  }
  
  public Stream<Interaction> getInteractions() {
    return this.interactions.stream();
  }
  
  public Optional<Interaction> findInteraction(HasGenePair genePair) {
    return this.getInteractions()
      .filter(interaction -> interaction.hasGenes(genePair))
      .findAny();
  }
  
  public Optional<Interaction> findInteraction(HasGenePairIds genePairIds) {
    return this.getInteractions()
      .filter(interaction -> interaction.hasGenes(genePairIds))
      .findAny();
  }
  
  public boolean hasInteraction(HasGenePair genePair) {
    return this.getInteractions()
      .anyMatch(interaction -> interaction.hasGenes(genePair));
  }
  
  public boolean hasInteraction(HasGenePairIds genePairIds) {
    return this.getInteractions()
      .anyMatch(interaction -> interaction.hasGenes(genePairIds));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!getClass().isAssignableFrom(obj.getClass()))
      return false;
    Interactome other = (Interactome) obj;
    if (getId() == null) {
      if (other.getId() != null)
        return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }
}
