/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.Interaction.InteractionId;

@Entity
@Table(name = "interaction")
@IdClass(InteractionId.class)
public class Interaction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "interactome", referencedColumnName = "id", nullable = false)
  private Interactome interactome;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "geneFrom", referencedColumnName = "id", nullable = false)
  private Gene geneFrom;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "geneTo", referencedColumnName = "id", nullable = false)
  private Gene geneTo;

  public Interactome getInteractome() {
    return interactome;
  }

  public Gene getGeneFrom() {
    return geneFrom;
  }

  public Gene getGeneTo() {
    return geneTo;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneFrom == null) ? 0 : geneFrom.hashCode());
    result = prime * result + ((geneTo == null) ? 0 : geneTo.hashCode());
    result = prime * result + ((interactome == null) ? 0 : interactome.hashCode());
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
    Interaction other = (Interaction) obj;
    if (geneFrom == null) {
      if (other.geneFrom != null)
        return false;
    } else if (!geneFrom.equals(other.geneFrom))
      return false;
    if (geneTo == null) {
      if (other.geneTo != null)
        return false;
    } else if (!geneTo.equals(other.geneTo))
      return false;
    if (interactome == null) {
      if (other.interactome != null)
        return false;
    } else if (!interactome.equals(other.interactome))
      return false;
    return true;
  }

  protected static class InteractionId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int interactome;
    private int geneFrom;
    private int geneTo;
    
    InteractionId() {}
    
    public InteractionId(int interactome, int geneFrom, int geneTo) {
      this.interactome = interactome;
      this.geneFrom = geneFrom;
      this.geneTo = geneTo;
    }

    public int getInteractome() {
      return interactome;
    }

    public int getGeneFrom() {
      return geneFrom;
    }

    public int getGeneTo() {
      return geneTo;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneFrom;
      result = prime * result + geneTo;
      result = prime * result + interactome;
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
      InteractionId other = (InteractionId) obj;
      if (geneFrom != other.geneFrom)
        return false;
      if (geneTo != other.geneTo)
        return false;
      if (interactome != other.interactome)
        return false;
      return true;
    }
  }
}
