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

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.execution.WorkStep.WorkStepId;
import org.sing_group.fluent.compare.Compare;

@Entity
@Table(name = "work_step")
@IdClass(WorkStepId.class)
public class WorkStep implements Serializable, Comparable<WorkStep> {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(
    name = "workId", referencedColumnName = "id",
    insertable = false, updatable = false,
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_work_work_step")
  )
  private Work work;
  
  @Id
  @Column(name = "stepOrder")
  private int order;
  
  @Column(name = "description", length = 255, nullable = true)
  private String description;
  
  @Column(name = "progress", scale = 1, precision = 2, nullable = true)
  private Double progress;

  WorkStep() {}

  public WorkStep(Work work, int order, String description, Double progress) {
    this.work = work;
    this.order = order;
    this.description = description;
    this.progress = progress;
  }

  public int getOrder() {
    return order;
  }
  
  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }
  
  public Optional<Double> getProgress() {
    return Optional.ofNullable(progress);
  }
  
  @Override
  public int compareTo(WorkStep o) {
    return Compare.objects(this, o)
      .by(WorkStep::getOrder)
    .andGet();
  }
  
  public static class WorkStepId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int work;
    
    private int order;
    
    WorkStepId() {}
    
    public WorkStepId(int work, int order) {
      this.work = work;
      this.order = order;
    }

    public int getWork() {
      return work;
    }

    public void setWork(int workId) {
      this.work = workId;
    }

    public int getOrder() {
      return order;
    }

    public void setOrder(int order) {
      this.order = order;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + order;
      result = prime * result + work;
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
      WorkStepId other = (WorkStepId) obj;
      if (order != other.order)
        return false;
      if (work != other.work)
        return false;
      return true;
    }
  }
}
