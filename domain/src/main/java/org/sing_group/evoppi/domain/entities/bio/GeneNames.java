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

import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.GeneNames.GeneNamesId;

@Entity
@Table(name = "gene_name")
@IdClass(GeneNamesId.class)
public class GeneNames implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "geneId")
  private int geneId;
  
  @Id
  @Column(name = "source", length = 255)
  private String source;
  
  @ElementCollection
  @CollectionTable(
    name = "gene_name_value",
    joinColumns = {
      @JoinColumn(name = "geneId", referencedColumnName = "geneId"),
      @JoinColumn(name = "geneSource", referencedColumnName = "source"),
    },
    foreignKey = @ForeignKey(name = "FK_gene_name_gene_name_values")
  )
  @Column(name = "name", length = 255)
  private Set<String> names;
  
  GeneNames() {}
  
  GeneNames(int geneId, String source, Collection<String> names) {
    requireNonEmpty(source, "source should be a non empty String");
    requireNonEmpty(names, "names should be a non empty collection");
    
    this.geneId = geneId;
    this.source = source;
    this.names = new HashSet<>(names);
  }

  public String getSource() {
    return source;
  }
  
  public Stream<String> getNames() {
    return names.stream();
  }
  
  public String getRepresentativeName() {
    return names.stream()
      .sorted()
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("No name found for gene: " + this.geneId));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneId;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
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
    GeneNames other = (GeneNames) obj;
    if (geneId != other.geneId)
      return false;
    if (source == null) {
      if (other.source != null)
        return false;
    } else if (!source.equals(other.source))
      return false;
    return true;
  }

  public static class GeneNamesId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int geneId;
    
    private String source;

    public int getGeneId() {
      return geneId;
    }

    public void setGeneId(int id) {
      this.geneId = id;
    }

    public String getSource() {
      return source;
    }

    public void setSource(String source) {
      this.source = source;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + geneId;
      result = prime * result + ((source == null) ? 0 : source.hashCode());
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
      GeneNamesId other = (GeneNamesId) obj;
      if (geneId != other.geneId)
        return false;
      if (source == null) {
        if (other.source != null)
          return false;
      } else if (!source.equals(other.source))
        return false;
      return true;
    }
  }
}
