/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "species-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "species-data", description = "Data of a species entity.")
public class SpeciesData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int id;
  
  private String name;

  @XmlElementWrapper(name = "interactomes", nillable = false, required = true)
  private IdAndUri[] interactomes;
  
  @XmlElementWrapper(name = "predictomes", nillable = false, required = true)
  private IdAndUri[] predictomes;
  
  SpeciesData() {}

  public SpeciesData(int id, String name, IdAndUri[] interactomes, IdAndUri[] predictomes) {
    this.id = id;
    this.name = name;
    this.interactomes = interactomes;
    this.predictomes = predictomes;
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

  public IdAndUri[] getInteractomes() {
    return interactomes;
  }

  public void setInteractomes(IdAndUri[] interactomes) {
    this.interactomes = interactomes;
  }
  
  public IdAndUri[] getPredictomes() {
    return predictomes;
  }
  
  public void setPredictomes(IdAndUri[] predictomes) {
    this.predictomes = predictomes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + Arrays.hashCode(predictomes);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    SpeciesData other = (SpeciesData) obj;
    if (id != other.id)
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (!Arrays.equals(predictomes, other.predictomes))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
