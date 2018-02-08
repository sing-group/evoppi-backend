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

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.domain.entities.execution.ExecutionStatusAndTime;
import org.sing_group.evoppi.domain.entities.execution.HasExecutionStatus;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", length = 4)
@Table(name = "interactions_result")
public abstract class InteractionsResult implements HasExecutionStatus {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;
  
  @Column(name = "queryGeneId", nullable = false)
  private int queryGeneId;
  
  @Column(name = "queryMaxDegree", nullable = false)
  private int queryMaxDegree;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
    name = "interactionsResultId", referencedColumnName = "id", foreignKey = @ForeignKey(
      name = "FK_interactions_result_interaction_group_result"
    )
  )
  private Set<InteractionGroupResult> interactions;
  
  @Embedded
  private ExecutionStatusAndTime status;

  InteractionsResult() {
    this.interactions = new HashSet<>();
    this.status = new ExecutionStatusAndTime();
    
  }
  
  public InteractionsResult(int queryGeneId, int queryMaxDegree) {
    this();
    
    this.queryGeneId = queryGeneId;
    this.queryMaxDegree = queryMaxDegree;
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

  public LocalDateTime getCreationDateTime() {
    return this.status.getCreationDateTime();
  }

  public Optional<LocalDateTime> getStartDateTime() {
    return this.status.getStartDateTime();
  }

  public Optional<LocalDateTime> getEndDateTime() {
    return this.status.getEndDateTime();
  }

  @Override
  public ExecutionStatus getStatus() {
    return this.status.getStatus();
  }

  @Override
  public void setScheduled() throws IllegalStateException {
    this.status.setScheduled();
  }
  
  @Override
  public void setRunning() throws IllegalStateException {
    this.status.setRunning();
  }

  @Override
  public void setFinished() throws IllegalStateException {
    this.status.setFinished();
  }

  @Override
  public void setFailed(String cause) throws IllegalStateException {
    this.status.setFailed(cause);
  }

  public Stream<InteractionGroupResult> getInteractions() {
    return interactions.stream();
  }

  public InteractionGroupResult addInteraction(int geneAId, int geneBId, Map<Integer, Integer> interactomeDegrees) {
    final InteractionGroupResult group = new InteractionGroupResult(this.getId(), geneAId, geneBId, interactomeDegrees);
    
    this.interactions.add(group);
    
    return group;
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
