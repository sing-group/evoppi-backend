/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "database_information")
public class DatabaseInformation implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;
  
  @Column
  private long speciesCount;
  
  @Column
  private long databaseInteractomesCount;
  
  @Column
  private long predictomesCount;
  
  @Column
  private long genesCount;
  
  @Column
  private long interactionsCount;

  private String version;

  DatabaseInformation() {}

  public DatabaseInformation(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }
  
  public long getSpeciesCount() {
    return speciesCount;
  }
  
  public long getDatabaseInteractomesCount() {
    return databaseInteractomesCount;
  }
  
  public long getPredictomesCount() {
    return predictomesCount;
  }
  
  public long getInteractionsCount() {
    return interactionsCount;
  }
  
  public long getGenesCount() {
    return genesCount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((version == null) ? 0 : version.hashCode());
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
    DatabaseInformation other = (DatabaseInformation) obj;
    if (id != other.id)
      return false;
    if (version == null) {
      if (other.version != null)
        return false;
    } else if (!version.equals(other.version))
      return false;
    return true;
  }
}
