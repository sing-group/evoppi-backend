/*-
 * #%L
 * REST
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

package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.bio.InteractomeType;
import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactome-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactome-data", description = "Data of an interactome entity.")
public class InteractomeData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;

  private String name;

  @XmlElement(name = "speciesA", required = true)
  private IdAndUri speciesA;

  @XmlElement(name = "speciesB", required = true)
  private IdAndUri speciesB;

  private InteractomeType interactomeType;

  InteractomeData() {}

  public InteractomeData(
    int id, String name, IdAndUri speciesA, IdAndUri speciesB, InteractomeType interactomeType
  ) {
    this.id = id;
    this.name = name;
    this.speciesA = speciesA;
    this.speciesB = speciesB;
    this.interactomeType = interactomeType;
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

  public IdAndUri getSpeciesA() {
    return speciesA;
  }

  public void setSpeciesA(IdAndUri speciesA) {
    this.speciesA = speciesA;
  }

  public IdAndUri getSpeciesB() {
    return speciesB;
  }

  public void setSpeciesB(IdAndUri speciesB) {
    this.speciesB = speciesB;
  }

  public InteractomeType getInteractomeType() {
    return interactomeType;
  }

  public void setInteractomeType(InteractomeType interactomeType) {
    this.interactomeType = interactomeType;
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
    InteractomeData other = (InteractomeData) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
