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
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.user.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interaction-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interaction-data", description = "Information of an interaction between two genes.")
public class InteractionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElementWrapper(name = "interactomes")
  @XmlElement(name = "interactomes", required = true)
  private IdAndUri[] interactomes;

  @XmlElement(name = "geneA", required = true)
  private IdAndUri geneA;

  @XmlElement(name = "geneB", required = true)
  private IdAndUri geneB;
  
  InteractionData() {}

  public InteractionData(IdAndUri geneA, IdAndUri geneB, IdAndUri[] interactome) {
    this.interactomes = interactome;
    this.geneA = geneA;
    this.geneB = geneB;
  }

  public IdAndUri[] getInteractomes() {
    return interactomes;
  }

  public void setInteractome(IdAndUri[] interactomes) {
    this.interactomes = interactomes;
  }

  public IdAndUri getGeneA() {
    return geneA;
  }

  public void setGeneA(IdAndUri geneA) {
    this.geneA = geneA;
  }

  public IdAndUri getGeneB() {
    return geneB;
  }

  public void setGeneB(IdAndUri geneB) {
    this.geneB = geneB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneA == null) ? 0 : geneA.hashCode());
    result = prime * result + ((geneB == null) ? 0 : geneB.hashCode());
    result = prime * result + Arrays.hashCode(interactomes);
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
    if (geneA == null) {
      if (other.geneA != null)
        return false;
    } else if (!geneA.equals(other.geneA))
      return false;
    if (geneB == null) {
      if (other.geneB != null)
        return false;
    } else if (!geneB.equals(other.geneB))
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    return true;
  }

}
