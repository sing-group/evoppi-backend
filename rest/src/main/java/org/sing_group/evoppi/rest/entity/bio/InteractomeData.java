package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.user.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactome-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactome-data", description = "Data of an interactome entity.")
public class InteractomeData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int id;
  
  private String name;

  @XmlElement(name = "species", required = true)
  private IdAndUri species;
  
  InteractomeData() {}

  public InteractomeData(int id, String name, IdAndUri species) {
    this.id = id;
    this.name = name;
    this.species = species;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IdAndUri getSpecies() {
    return species;
  }

  public void setSpecies(IdAndUri species) {
    this.species = species;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((species == null) ? 0 : species.hashCode());
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
    InteractomeData other = (InteractomeData) obj;
    if (id != other.id)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }

}
