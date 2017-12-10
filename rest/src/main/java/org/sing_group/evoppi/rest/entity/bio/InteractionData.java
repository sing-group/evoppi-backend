/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.user.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interaction-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interaction-data", description = "Information of an interaction between two genes.")
public class InteractionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "interactome", required = true)
  private IdAndUri interactome;

  @XmlElement(name = "geneFrom", required = true)
  private IdAndUri geneFrom;

  @XmlElement(name = "geneTo", required = true)
  private IdAndUri geneTo;
  
  InteractionData() {}

  public InteractionData(IdAndUri interactome, IdAndUri geneFrom, IdAndUri geneTo) {
    this.interactome = interactome;
    this.geneFrom = geneFrom;
    this.geneTo = geneTo;
  }

  public IdAndUri getInteractome() {
    return interactome;
  }

  public void setInteractome(IdAndUri interactome) {
    this.interactome = interactome;
  }

  public IdAndUri getGeneFrom() {
    return geneFrom;
  }

  public void setGeneFrom(IdAndUri geneFrom) {
    this.geneFrom = geneFrom;
  }

  public IdAndUri getGeneTo() {
    return geneTo;
  }

  public void setGeneTo(IdAndUri geneTo) {
    this.geneTo = geneTo;
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
    InteractionData other = (InteractionData) obj;
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

}
