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
package org.sing_group.evoppi.domain.entities.execution;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "work")
public class Work implements HasExecutionStatus, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = true)
  private String description;

  @Column(name = "resultReference", length = 1023, nullable = true)
  private String resultReference;

  @OneToMany(mappedBy = "work", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("order ASC")
  private SortedSet<WorkStep> steps;
  
  @Embedded
  private ExecutionStatusAndTime status;
  
  @Transient
  private final ReentrantReadWriteLock stepsLock;

  Work() {
    this.stepsLock = new ReentrantReadWriteLock();
    this.status = new ExecutionStatusAndTime();
  }
  
  public Work(String name) {
    this();
    
    this.name = name;
    this.steps = new TreeSet<>();
  }

  public Work(String name, String description, String resultReference) {
    this(name);
    
    this.description = description;
    this.resultReference = resultReference;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
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

  public Optional<String> getResultReference() {
    return Optional.ofNullable(resultReference);
  }

  public Stream<WorkStep> getSteps() {
    this.stepsLock.readLock().lock();
    
    try {
      return steps.stream();
    } finally {
      this.stepsLock.readLock().unlock();
    }
  }
  
  public WorkStep addStep(String description, Double progress) {
    this.stepsLock.writeLock().lock();
    
    try {
      final WorkStep step = new WorkStep(this, this.steps.size() + 1, description, progress);
      
      this.steps.add(step);
      
      return step;
    } finally {
      this.stepsLock.writeLock().unlock();
    }
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
}
