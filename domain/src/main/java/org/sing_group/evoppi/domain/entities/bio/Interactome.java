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

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePairIds;

@Entity
@Table(
  name = "interactome", uniqueConstraints = @UniqueConstraint(columnNames = {
    "name", "speciesA", "speciesB"
  }), indexes = @Index(columnList = "id, speciesA, speciesB")
)
@Inheritance(strategy = InheritanceType.JOINED)
public class Interactome implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "speciesA", referencedColumnName = "id", nullable = false)
  private Species speciesA;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "speciesB", referencedColumnName = "id", nullable = false)
  private Species speciesB;

  @OneToMany(mappedBy = "interactome", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactions;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "collection", referencedColumnName = "id", nullable = true)
  private InteractomeCollection collection;

  @Transient
  protected final ReentrantReadWriteLock interactionsLock;

  Interactome() {
    this.interactionsLock = new ReentrantReadWriteLock();
  }

  protected Interactome(String name, Species speciesA, Species speciesB, InteractomeCollection collection) {
    this.name = name;
    this.speciesA = speciesA;
    this.speciesB = speciesB;
    this.interactions = new HashSet<>();
    this.collection = collection;
    this.interactionsLock = new ReentrantReadWriteLock();
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Species getSpeciesA() {
    return speciesA;
  }

  public Species getSpeciesB() {
    return speciesB;
  }

  public Stream<Interaction> getInteractions() {
    return this.interactions.stream();
  }
  
  public InteractomeCollection getCollection() {
    return collection;
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

  public Interaction addInteraction(
    Species speciesA, Species speciesB, Gene geneA, Gene geneB, GeneInInteractome geneAInInteractome, GeneInInteractome geneBInInteractome
  ) {
    this.interactionsLock.writeLock().lock();

    try {
      final Interaction interaction =
        new Interaction(speciesA, speciesB, this, geneA, geneB, geneAInInteractome, geneBInInteractome);
      this.interactions.add(interaction);

      return interaction;
    } finally {
      this.interactionsLock.writeLock().unlock();
    }
  }
  
  public InteractomeType getInteractomeType() {
    throw new RuntimeException("This method must be called on subclasses");
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
    if (!Interactome.class.isAssignableFrom(obj.getClass()))
      return false;

    Interactome other = Interactome.class.cast(obj);
    if (getId() == null) {
      if (other.getId() != null)
        return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }
}
