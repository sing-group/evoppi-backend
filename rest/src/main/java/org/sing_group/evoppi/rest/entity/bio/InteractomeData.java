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

import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interactome-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interactome-data", description = "Data of an interactome entity.")
public class InteractomeData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;

  private String name;

  private String dbSourceIdType;

  private Integer numOriginalInteractions;

  private Integer numUniqueOriginalInteractions;

  private Integer numUniqueOriginalGenes;

  private Integer numInteractionsNotToUniProtKB;

  private Integer numGenesNotToUniProtKB;

  private Integer numInteractionsNotToGeneId;

  private Integer numGenesNotToGeneId;

  private Integer numFinalInteractions;

  private Double probFinalInteractions;

  private Integer numRemovedInterSpeciesInteractions;

  private Integer numMultimappedToGeneId;

  @XmlElement(name = "species", required = true)
  private IdAndUri species;

  InteractomeData() {}

  public InteractomeData(
    int id, String name, IdAndUri species, String dbSourceIdType, Integer numOriginalInteractions,
    Integer numUniqueOriginalInteractions, Integer numUniqueOriginalGenes,
    Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Double probFinalInteractions, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId
  ) {
    this.id = id;
    this.name = name;
    this.species = species;
    this.dbSourceIdType = dbSourceIdType;
    this.numOriginalInteractions = numOriginalInteractions;
    this.numUniqueOriginalInteractions = numUniqueOriginalInteractions;
    this.numUniqueOriginalGenes = numUniqueOriginalGenes;
    this.numInteractionsNotToUniProtKB = numInteractionsNotToUniProtKB;
    this.numGenesNotToUniProtKB = numGenesNotToUniProtKB;
    this.numInteractionsNotToGeneId = numInteractionsNotToGeneId;
    this.numGenesNotToGeneId = numGenesNotToGeneId;
    this.numFinalInteractions = numFinalInteractions;
    this.probFinalInteractions = probFinalInteractions;
    this.numRemovedInterSpeciesInteractions = numRemovedInterSpeciesInteractions;
    this.numMultimappedToGeneId = numMultimappedToGeneId;
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

  public String getDbSourceIdType() {
    return dbSourceIdType;
  }

  public void setDbSourceIdType(String dbSourceIdType) {
    this.dbSourceIdType = dbSourceIdType;
  }

  public Integer getNumOriginalInteractions() {
    return numOriginalInteractions;
  }

  public void setNumOriginalInteractions(Integer numOriginalInteractions) {
    this.numOriginalInteractions = numOriginalInteractions;
  }

  public Integer getNumUniqueOriginalInteractions() {
    return numUniqueOriginalInteractions;
  }

  public void setNumUniqueOriginalInteractions(Integer numUniqueOriginalInteractions) {
    this.numUniqueOriginalInteractions = numUniqueOriginalInteractions;
  }

  public Integer getNumUniqueOriginalGenes() {
    return numUniqueOriginalGenes;
  }

  public void setNumUniqueOriginalGenes(Integer numUniqueOriginalGenes) {
    this.numUniqueOriginalGenes = numUniqueOriginalGenes;
  }

  public Integer getNumInteractionsNotToUniProtKB() {
    return numInteractionsNotToUniProtKB;
  }

  public void setNumInteractionsNotToUniProtKB(Integer numInteractionsNotToUniProtKB) {
    this.numInteractionsNotToUniProtKB = numInteractionsNotToUniProtKB;
  }

  public Integer getNumGenesNotToUniProtKB() {
    return numGenesNotToUniProtKB;
  }

  public void setNumGenesNotToUniProtKB(Integer numGenesNotToUniProtKB) {
    this.numGenesNotToUniProtKB = numGenesNotToUniProtKB;
  }

  public Integer getNumInteractionsNotToGeneId() {
    return numInteractionsNotToGeneId;
  }

  public void setNumInteractionsNotToGeneId(Integer numInteractionsNotToGeneId) {
    this.numInteractionsNotToGeneId = numInteractionsNotToGeneId;
  }

  public Integer getNumGenesNotToGeneId() {
    return numGenesNotToGeneId;
  }

  public void setNumGenesNotToGeneId(Integer numGenesNotToGeneId) {
    this.numGenesNotToGeneId = numGenesNotToGeneId;
  }

  public Integer getNumFinalInteractions() {
    return numFinalInteractions;
  }

  public void setNumFinalInteractions(Integer numFinalInteractions) {
    this.numFinalInteractions = numFinalInteractions;
  }

  public Double getProbFinalInteractions() {
    return probFinalInteractions;
  }

  public void setProbFinalInteractions(Double probFinalInteractions) {
    this.probFinalInteractions = probFinalInteractions;
  }

  public Integer getNumRemovedInterSpeciesInteractions() {
    return numRemovedInterSpeciesInteractions;
  }

  public void setNumRemovedInterSpeciesInteractions(Integer numRemovedInterSpeciesInteractions) {
    this.numRemovedInterSpeciesInteractions = numRemovedInterSpeciesInteractions;
  }

  public Integer getNumMultimappedToGeneId() {
    return numMultimappedToGeneId;
  }

  public void setNumMultimappedToGeneId(Integer numMultimappedToGeneId) {
    this.numMultimappedToGeneId = numMultimappedToGeneId;
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
    result = prime * result + ((dbSourceIdType == null) ? 0 : dbSourceIdType.hashCode());
    result = prime * result + id;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((numFinalInteractions == null) ? 0 : numFinalInteractions.hashCode());
    result = prime * result + ((numGenesNotToGeneId == null) ? 0 : numGenesNotToGeneId.hashCode());
    result = prime * result + ((numGenesNotToUniProtKB == null) ? 0 : numGenesNotToUniProtKB.hashCode());
    result = prime * result + ((numInteractionsNotToGeneId == null) ? 0 : numInteractionsNotToGeneId.hashCode());
    result = prime * result + ((numInteractionsNotToUniProtKB == null) ? 0 : numInteractionsNotToUniProtKB.hashCode());
    result = prime * result + ((numMultimappedToGeneId == null) ? 0 : numMultimappedToGeneId.hashCode());
    result = prime * result + ((numOriginalInteractions == null) ? 0 : numOriginalInteractions.hashCode());
    result =
      prime * result
        + ((numRemovedInterSpeciesInteractions == null) ? 0 : numRemovedInterSpeciesInteractions.hashCode());
    result = prime * result + ((numUniqueOriginalGenes == null) ? 0 : numUniqueOriginalGenes.hashCode());
    result = prime * result + ((numUniqueOriginalInteractions == null) ? 0 : numUniqueOriginalInteractions.hashCode());
    result = prime * result + ((probFinalInteractions == null) ? 0 : probFinalInteractions.hashCode());
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
    if (dbSourceIdType == null) {
      if (other.dbSourceIdType != null)
        return false;
    } else if (!dbSourceIdType.equals(other.dbSourceIdType))
      return false;
    if (id != other.id)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (numFinalInteractions == null) {
      if (other.numFinalInteractions != null)
        return false;
    } else if (!numFinalInteractions.equals(other.numFinalInteractions))
      return false;
    if (numGenesNotToGeneId == null) {
      if (other.numGenesNotToGeneId != null)
        return false;
    } else if (!numGenesNotToGeneId.equals(other.numGenesNotToGeneId))
      return false;
    if (numGenesNotToUniProtKB == null) {
      if (other.numGenesNotToUniProtKB != null)
        return false;
    } else if (!numGenesNotToUniProtKB.equals(other.numGenesNotToUniProtKB))
      return false;
    if (numInteractionsNotToGeneId == null) {
      if (other.numInteractionsNotToGeneId != null)
        return false;
    } else if (!numInteractionsNotToGeneId.equals(other.numInteractionsNotToGeneId))
      return false;
    if (numInteractionsNotToUniProtKB == null) {
      if (other.numInteractionsNotToUniProtKB != null)
        return false;
    } else if (!numInteractionsNotToUniProtKB.equals(other.numInteractionsNotToUniProtKB))
      return false;
    if (numMultimappedToGeneId == null) {
      if (other.numMultimappedToGeneId != null)
        return false;
    } else if (!numMultimappedToGeneId.equals(other.numMultimappedToGeneId))
      return false;
    if (numOriginalInteractions == null) {
      if (other.numOriginalInteractions != null)
        return false;
    } else if (!numOriginalInteractions.equals(other.numOriginalInteractions))
      return false;
    if (numRemovedInterSpeciesInteractions == null) {
      if (other.numRemovedInterSpeciesInteractions != null)
        return false;
    } else if (!numRemovedInterSpeciesInteractions.equals(other.numRemovedInterSpeciesInteractions))
      return false;
    if (numUniqueOriginalGenes == null) {
      if (other.numUniqueOriginalGenes != null)
        return false;
    } else if (!numUniqueOriginalGenes.equals(other.numUniqueOriginalGenes))
      return false;
    if (numUniqueOriginalInteractions == null) {
      if (other.numUniqueOriginalInteractions != null)
        return false;
    } else if (!numUniqueOriginalInteractions.equals(other.numUniqueOriginalInteractions))
      return false;
    if (probFinalInteractions == null) {
      if (other.probFinalInteractions != null)
        return false;
    } else if (!probFinalInteractions.equals(other.probFinalInteractions))
      return false;
    if (species == null) {
      if (other.species != null)
        return false;
    } else if (!species.equals(other.species))
      return false;
    return true;
  }

}
