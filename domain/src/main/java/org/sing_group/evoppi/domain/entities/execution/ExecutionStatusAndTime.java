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

import static javax.persistence.EnumType.STRING;
import static org.sing_group.evoppi.domain.entities.execution.ExecutionStatus.COMPLETED;
import static org.sing_group.evoppi.domain.entities.execution.ExecutionStatus.FAILED;
import static org.sing_group.evoppi.domain.entities.execution.ExecutionStatus.RUNNING;
import static org.sing_group.evoppi.domain.entities.execution.ExecutionStatus.SCHEDULED;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Embeddable
public class ExecutionStatusAndTime implements HasExecutionStatus, Serializable {
  private static final long serialVersionUID = 1L;

  @Enumerated(STRING)
  @Column(name = "status", length = 9, nullable = false)
  private ExecutionStatus status;
  
  @Column(name = "creationDateTime", nullable = false)
  private LocalDateTime creationDateTime;

  @Column(name = "startDateTime", nullable = true)
  private LocalDateTime startDateTime;

  @Column(name = "endDateTime", nullable = true)
  private LocalDateTime endDateTime;

  @Column(name = "failureCause", length = 255, nullable = true)
  private String failureCause;
  
  @Transient
  private final ReentrantReadWriteLock statusLock;

  public ExecutionStatusAndTime() {
    this.status = ExecutionStatus.CREATED;
    this.creationDateTime = LocalDateTime.now();
    this.statusLock = new ReentrantReadWriteLock();
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public Optional<LocalDateTime> getStartDateTime() {
    return Optional.ofNullable(startDateTime);
  }

  public Optional<LocalDateTime> getEndDateTime() {
    return Optional.ofNullable(endDateTime);
  }

  @Override
  public ExecutionStatus getStatus() {
    this.statusLock.readLock().lock();
    
    try {
      return this.status;
    } finally {
      this.statusLock.readLock().unlock();
    }
  }

  @Override
  public void setRunning() throws IllegalStateException {
    this.statusLock.writeLock().lock();

    try {
      try {
        final ExecutionStatus previousStatus = this.status;

        this.status = this.status.changeTo(RUNNING);

        if (previousStatus != this.status)
          this.startDateTime = LocalDateTime.now();
      } catch (IllegalArgumentException iae) {
        throw new IllegalStateException(iae.getMessage());
      }
    } finally {
      this.statusLock.writeLock().unlock();
    }
  }

  @Override
  public void setFinished() throws IllegalStateException {
    this.statusLock.writeLock().lock();

    try {
      try {
        this.status = this.status.changeTo(COMPLETED);
        this.endDateTime = LocalDateTime.now();
      } catch (IllegalArgumentException iae) {
        throw new IllegalStateException(iae.getMessage());
      }
    } finally {
      this.statusLock.writeLock().unlock();
    }
  }

  @Override
  public void setFailed(String cause) throws IllegalStateException {
    this.statusLock.writeLock().lock();

    try {
      try {
        this.status = this.status.changeTo(FAILED);
        
        this.failureCause = cause;
        this.endDateTime = LocalDateTime.now();
      } catch (IllegalArgumentException iae) {
        throw new IllegalStateException(iae.getMessage());
      }
    } finally {
      this.statusLock.writeLock().unlock();
    }
  }

  @Override
  public void setScheduled() throws IllegalStateException {
    this.statusLock.writeLock().lock();

    try {
      try {
        this.status = this.status.changeTo(SCHEDULED);
      } catch (IllegalArgumentException iae) {
        throw new IllegalStateException(iae.getMessage());
      }
    } finally {
      this.statusLock.writeLock().unlock();
    }
  }
}
