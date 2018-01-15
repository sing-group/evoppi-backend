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
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gene")
public class Gene implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private int id;
  
  @Lob
  @Column(name = "sequence", nullable = false)
  private String sequence;
  
  @OneToMany(mappedBy = "geneA", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsA;
  
  @OneToMany(mappedBy = "geneB", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsB;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "geneId", referencedColumnName = "id")
  private Set<GeneNames> names;
  
  public int getId() {
    return id;
  }
  
  public String getSequence() {
    return sequence;
  }
  
  public Stream<Interaction> getInteractions() {
    return Stream.concat(this.interactsA.stream(), this.interactsB.stream())
      .distinct();
  }
  
  public boolean hasInteraction(Interaction interaction) {
    return this.interactsA.contains(interaction) || this.interactsB.contains(interaction);
  }

  public Stream<GeneNames> getNames() {
    return this.names.stream();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!getClass().isAssignableFrom(obj.getClass()))
      return false;
    Gene other = (Gene) obj;
    if (getId() != other.getId())
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    return Integer.toString(this.id);
  }
}
