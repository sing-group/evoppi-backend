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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactome-collection-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactome-collection-data", description = "Data of an interactome collection entity.")
public class InteractomeCollectionData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private long databaseInteractomeCount;
  private long predictomeCount;

  InteractomeCollectionData() {}

  public InteractomeCollectionData(int id, String name, long databaseInteractomeCount, long predictomeCount) {
    super();
    this.id = id;
    this.name = name;
    this.databaseInteractomeCount = databaseInteractomeCount;
    this.predictomeCount = predictomeCount;
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

  public long getDatabaseInteractomeCount() {
    return databaseInteractomeCount;
  }

  public void setDatabaseInteractomeCount(long databaseInteractomeCount) {
    this.databaseInteractomeCount = databaseInteractomeCount;
  }

  public long getPredictomeCount() {
    return predictomeCount;
  }

  public void setPredictomeCount(long predictomeCount) {
    this.predictomeCount = predictomeCount;
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
    InteractomeCollectionData other = (InteractomeCollectionData) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
