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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gene",
  indexes = @Index(columnList = "id, species")
)
public class Gene implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int id;
  
  @Column(name = "defaultName", length = 255, nullable = false)
  private String defaultName;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "geneId", referencedColumnName = "id")
  private Set<GeneNames> names;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "species", referencedColumnName = "id", nullable = false)
  private Species species;

  @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GeneSequence> geneSequence;

  @OneToMany(mappedBy = "geneA", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsA;

  @OneToMany(mappedBy = "geneB", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsB;

  Gene() {}
  
  public Gene(Species species, int id, String defaultName, Set<GeneNames> names) {
    this.id = id;
    this.species = species;
    this.defaultName = defaultName;
    this.names = names;
    this.interactsA = new HashSet<>();
    this.interactsB = new HashSet<>();
    this.geneSequence = new HashSet<>();
  }
  
  public int getId() {
    return id;
  }
  
  public String getDefaultName() {
    return defaultName;
  }

  public Stream<Interaction> getInteractions() {
    return Stream.concat(this.interactsA.stream(), this.interactsB.stream())
      .distinct();
  }
  
  public Stream<Interaction> getInteractionsOfInteractome(Interactome interactome) {
    return this.getInteractionsOfInteractome(interactome.getId());
  }
  
  public Stream<Interaction> getInteractionsOfInteractome(int interactomeId) {
    return this.getInteractions()
      .filter(interaction -> interaction.getInteractome().getId() == interactomeId);
  }

  public boolean hasInteraction(Interaction interaction) {
    return this.interactsA.contains(interaction) || this.interactsB.contains(interaction);
  }
  
  public boolean belongsToInteractome(int interactomeId) {
    return this.getInteractions()
      .map(Interaction::getInteractome)
      .mapToInt(Interactome::getId)
    .anyMatch(id -> id == interactomeId);
  }
  
  public Stream<GeneSequence> getGeneSequence() {
    return geneSequence.stream();
  }
  
  public Stream<String> getSequences() {
    return this.getGeneSequence()
      .sorted((g1, g2) -> g1.getVersion() - g2.getVersion())
      .map(GeneSequence::getSequence);
  }

  public Stream<GeneNames> getNames() {
    return this.names.stream();
  }
  
  public Species getSpecies() {
    return species;
  }
  
  public void addGeneSequence(String sequence) {
    this.geneSequence.add(new GeneSequence(this, this.geneSequence.size()+1, sequence));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    Gene other = (Gene) obj;
    if (getId() != other.getId())
      return false;
    return true;
  }

  @Override
  public String toString() {
    return Integer.toString(this.id);
  }
}
