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
package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.GeneSequence.GeneSequenceId;

@Entity
@Table(name = "gene_sequence")
@IdClass(GeneSequenceId.class)
public class GeneSequence implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "geneId", referencedColumnName = "id", nullable = false)
  private Gene gene;

  @Id
  private int version;

  @Lob
  @Column(name = "sequence", nullable = false)
  private String sequence;

  public Gene getGene() {
    return gene;
  }

  public int getVersion() {
    return version;
  }

  public String getSequence() {
    return sequence;
  }

  public final static class GeneSequenceId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int gene;

    private int version;

    GeneSequenceId() {}

    public GeneSequenceId(int gene, int version) {
      this.gene = gene;
      this.version = version;
    }

    public int getGene() {
      return gene;
    }

    public void setGene(int gene) {
      this.gene = gene;
    }

    public int getVersion() {
      return version;
    }

    public void setVersion(int version) {
      this.version = version;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + gene;
      result = prime * result + version;
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
      GeneSequenceId other = (GeneSequenceId) obj;
      if (gene != other.gene)
        return false;
      if (version != other.version)
        return false;
      return true;
    }

  }
}
