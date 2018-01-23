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

import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.IntStream;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("SAME")
@Table(name = "same_species_interactions_result")
public class SameSpeciesInteractionsResult extends InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "same_species_interactions_result_query_interactome",
    joinColumns = @JoinColumn(name = "interactionsResultId", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "FK_same_species_interactions_result_query_interactome")
  )
  @Column(name = "interactomeId", nullable = false)
  private Set<Integer> queryInteractomeIds;

  SameSpeciesInteractionsResult() {}
  
  public SameSpeciesInteractionsResult(int queryGeneId, int queryMaxDegree, int[] queryInteractomeIds) {
    super(queryGeneId, queryMaxDegree);
    
    this.queryInteractomeIds = IntStream.of(queryInteractomeIds).boxed().collect(toSet());
  }

  public IntStream getQueryInteractomeIds() {
    return queryInteractomeIds.stream().mapToInt(Integer::intValue);
  }
}
