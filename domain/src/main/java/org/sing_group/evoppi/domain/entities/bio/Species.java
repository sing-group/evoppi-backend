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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "species")
public class Species implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  @Column(name = "name", length = 100, nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "species", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interactome> interactomes;
  
  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Stream<Interactome> getInteractomes() {
    return this.interactomes.stream();
  }
  
  public boolean hasInteractome(Interactome interactome) {
    return this.interactomes.contains(interactome);
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
    Species other = (Species) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
