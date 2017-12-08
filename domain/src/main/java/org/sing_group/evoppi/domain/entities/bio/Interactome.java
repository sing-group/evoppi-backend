package org.sing_group.evoppi.domain.entities.bio;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "interactome")
public class Interactome implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @Column(name = "name", length = 100, nullable = false, unique = true)
  private String name;
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "specieId", referencedColumnName = "id", nullable = false)
  private Specie specie;

  @OneToMany(mappedBy = "interactome", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactions;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Specie getSpecie() {
    return specie;
  }
  
  public Stream<Interaction> getInteractions() {
    return this.interactions.stream();
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
    Interactome other = (Interactome) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
