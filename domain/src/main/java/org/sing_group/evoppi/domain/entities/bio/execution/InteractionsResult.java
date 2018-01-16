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
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "interactions_result")
public class InteractionsResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @Column(name = "queryGeneId", nullable = false)
  private int queryGeneId;
  
  @Column(name = "queryMaxDegree", nullable = false)
  private int queryMaxDegree;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "interactions_result_query_interactome",
    joinColumns = @JoinColumn(name = "interactionsResultId", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "FK_interactions_result_query_interactome")
  )
  @Column(name = "interactomeId", nullable = false)
  private Set<Integer> queryInteractomeIds;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
    name = "interactionsResultId",
    referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "FK_interactions_result_interaction_group_result")
  )
  private Set<InteractionGroupResult> interactions;
  
  @Column(name = "creationDateTime", nullable = false)
  private LocalDateTime creationDateTime;
  
  @Column(name = "status", length = 9, nullable = false)
  @Enumerated(EnumType.STRING)
  private ExecutionStatus status;

  InteractionsResult() {}
  
  public InteractionsResult(int queryGeneId, int queryMaxDegree, int[] queryInteractomeIds) {
    this.queryGeneId = queryGeneId;
    this.queryMaxDegree = queryMaxDegree;
    this.queryInteractomeIds = IntStream.of(queryInteractomeIds).boxed().collect(toSet());
    this.creationDateTime = LocalDateTime.now();
    this.status = ExecutionStatus.CREATED;
  }

  public Integer getId() {
    return id;
  }

  public int getQueryGeneId() {
    return queryGeneId;
  }

  public int getQueryMaxDegree() {
    return queryMaxDegree;
  }

  public IntStream getQueryInteractomeIds() {
    return queryInteractomeIds.stream().mapToInt(Integer::intValue);
  }

  public Stream<InteractionGroupResult> getInteractions() {
    return interactions.stream();
  }
  
  public InteractionGroupResult addInteraction(
    int geneAId, int geneBId, int degree, int[] interactomeIds
  ) {
    final Set<Integer> interactomeIdsCollection = IntStream.of(interactomeIds).boxed().collect(toSet());
    
    final InteractionGroupResult group = new InteractionGroupResult(this.id, geneAId, geneBId, degree, interactomeIdsCollection);
    
    this.interactions.add(group);
    
    return group;
  }
  
  public ExecutionStatus getStatus() {
    return status;
  }
  
  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }
  
  public void scheduled() {
    this.checkAndChangeStatus(ExecutionStatus.CREATED, ExecutionStatus.SCHEDULED);
  }
  
  public void running() {
    this.checkAndChangeStatus(ExecutionStatus.SCHEDULED, ExecutionStatus.RUNNING);
  }
  
  public void completed() {
    this.checkAndChangeStatus(ExecutionStatus.RUNNING, ExecutionStatus.COMPLETED);
  }
  
  public void failed() {
    this.checkAndChangeStatus(ExecutionStatus.RUNNING, ExecutionStatus.FAILED);
  }
  
  private void checkAndChangeStatus(ExecutionStatus expectedActual, ExecutionStatus requested) {
    if (this.status != expectedActual)
      throw new IllegalStateException(String.format("Invalid status change, from %s to %s", this.status, requested));
    
    this.status = requested;
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
    if (getClass() != obj.getClass())
      return false;
    InteractionsResult other = (InteractionsResult) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
