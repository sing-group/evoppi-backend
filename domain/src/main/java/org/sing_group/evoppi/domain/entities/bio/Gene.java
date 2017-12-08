package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
  
  @OneToMany(mappedBy = "geneFrom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsWith;
  
  @OneToMany(mappedBy = "geneTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactedBy;
  
  public int getId() {
    return id;
  }
  
  public String getSequence() {
    return sequence;
  }

  public Stream<Interaction> getInteractsWith() {
    return this.interactsWith.stream();
  }

  public boolean hasInteractionWith(Interaction interaction) {
    return this.interactsWith.contains(interaction);
  }

  public Stream<Interaction> getInteractedBy() {
    return this.interactedBy.stream();
  }

  public boolean hasInteractedBy(Interaction interaction) {
    return this.interactedBy.contains(interaction);
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
    if (getClass() != obj.getClass())
      return false;
    Gene other = (Gene) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
