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

package org.sing_group.evoppi.rest.entity.info;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "stats-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "stats-data", description = "Statistic information about the database.")
public class StatsData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private long speciesCount;
  private long interactomesCount;
  private long genesCount;
  private long interactionsCount;

  StatsData() {}
  
  public StatsData(long speciesCount, long interactomesCount, long genesCount, long interactionsCount) {
    this.speciesCount = speciesCount;
    this.interactomesCount = interactomesCount;
    this.genesCount = genesCount;
    this.interactionsCount = interactionsCount;
  }

  public long getSpeciesCount() {
    return speciesCount;
  }

  public void setSpeciesCount(long speciesCount) {
    this.speciesCount = speciesCount;
  }

  public long getInteractomesCount() {
    return interactomesCount;
  }

  public void setInteractomesCount(long interactomesCount) {
    this.interactomesCount = interactomesCount;
  }

  public long getGenesCount() {
    return genesCount;
  }

  public void setGenesCount(long genesCount) {
    this.genesCount = genesCount;
  }

  public long getInteractionsCount() {
    return interactionsCount;
  }

  public void setInteractionsCount(long interactionsCount) {
    this.interactionsCount = interactionsCount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (genesCount ^ (genesCount >>> 32));
    result = prime * result + (int) (interactomesCount ^ (interactomesCount >>> 32));
    result = prime * result + (int) (interactionsCount ^ (interactionsCount >>> 32));
    result = prime * result + (int) (speciesCount ^ (speciesCount >>> 32));
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
    StatsData other = (StatsData) obj;
    if (genesCount != other.genesCount)
      return false;
    if (interactomesCount != other.interactomesCount)
      return false;
    if (interactionsCount != other.interactionsCount)
      return false;
    if (speciesCount != other.speciesCount)
      return false;
    return true;
  }

}
