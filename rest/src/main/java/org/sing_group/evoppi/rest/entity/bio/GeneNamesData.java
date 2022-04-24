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
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "gene-names-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "gene-names-data", description = "Information of gene names.")
public class GeneNamesData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int geneId;
  
  private String defaultName;
  
  private GeneNameData[] names;
  
  private IdAndUri[] interactomes;

  GeneNamesData() {}

  public GeneNamesData(int id, String defaultName, GeneNameData[] names, IdAndUri[] interactomes) {
    this.geneId = id;
    this.defaultName = defaultName;
    this.names = names;
    this.interactomes = interactomes;
  }

  public int getGeneId() {
    return geneId;
  }

  public void setGeneId(int id) {
    this.geneId = id;
  }
  
  public String getDefaultName() {
    return defaultName;
  }

  public void setDefaultName(String defaultName) {
    this.defaultName = defaultName;
  }

  public GeneNameData[] getNames() {
    return names;
  }

  public void setNames(GeneNameData[] names) {
    this.names = names;
  }

  public IdAndUri[] getInteractomes() {
    return interactomes;
  }

  public void setInteractomes(IdAndUri[] interactomes) {
    this.interactomes = interactomes;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((defaultName == null) ? 0 : defaultName.hashCode());
    result = prime * result + geneId;
    result = prime * result + Arrays.hashCode(interactomes);
    result = prime * result + Arrays.hashCode(names);
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
    GeneNamesData other = (GeneNamesData) obj;
    if (defaultName == null) {
      if (other.defaultName != null)
        return false;
    } else if (!defaultName.equals(other.defaultName))
      return false;
    if (geneId != other.geneId)
      return false;
    if (!Arrays.equals(interactomes, other.interactomes))
      return false;
    if (!Arrays.equals(names, other.names))
      return false;
    return true;
  }
}
