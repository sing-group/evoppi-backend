/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "gene-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "gene-data", description = "Information of a gene.")
public class GeneData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  
  private String sequence;

  private GeneNameData[] names;

  GeneData() {}
  
  public GeneData(int id, String sequence, GeneNameData[] names) {
    this.id = id;
    this.sequence = sequence;
    this.names = names;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  public GeneNameData[] getNames() {
    return names;
  }

  public void setNames(GeneNameData[] names) {
    this.names = names;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + Arrays.hashCode(names);
    result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
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
    GeneData other = (GeneData) obj;
    if (id != other.id)
      return false;
    if (!Arrays.equals(names, other.names))
      return false;
    if (sequence == null) {
      if (other.sequence != null)
        return false;
    } else if (!sequence.equals(other.sequence))
      return false;
    return true;
  }
}
