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

package org.sing_group.evoppi.domain.entities.bio.execution;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.user.User;

@Entity
@Table(name = "same_species_interactions_result")
public class SameSpeciesInteractionsResult extends InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "same_species_interactions_result_query_interactome",
    joinColumns = @JoinColumn(name = "resultId", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "queryInteractomesId", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "FK_same_species_interactions_result_query_interactome")
  )
  private Set<Interactome> queryInteractomes;

  SameSpeciesInteractionsResult() {}
  
  public SameSpeciesInteractionsResult(
    String name, String description, String resultReference,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes, User owner
  ) {
    super(name, description, resultReference, queryGene, queryMaxDegree, owner);
    
    this.queryInteractomes = new HashSet<>(queryInteractomes);
  }
  
  public SameSpeciesInteractionsResult(
    String name, String description, Function<String, String> resultReferenceBuilder,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes, User owner
  ) {
    super(name, description, resultReferenceBuilder, queryGene, queryMaxDegree, owner);
    
    this.queryInteractomes = new HashSet<>(queryInteractomes);
  }

  public Species getQuerySpecies() {
    return this.getQueryInteractomes()
      .findAny()
      .map(Interactome::getSpeciesA)
    .orElseThrow(() -> new IllegalStateException("Species could not retrieved because there is not any query interactome"));
  }

  public Stream<Interactome> getQueryInteractomes() {
    return queryInteractomes.stream();
  }
  
  public IntStream getQueryInteractomeIds() {
    return getQueryInteractomes().mapToInt(Interactome::getId);
  }

  public boolean hasInteractome(int id) {
    return this.getQueryInteractomeIds()
      .anyMatch(interactomeId -> interactomeId == id);
  }
}
