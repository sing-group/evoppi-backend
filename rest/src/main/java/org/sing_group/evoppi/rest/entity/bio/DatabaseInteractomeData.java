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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.bio.InteractomeType;
import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "database-interactome", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "database-interactome", description = "Data of a database interactome entity."
)
public class DatabaseInteractomeData extends InteractomeData {
  private static final long serialVersionUID = 1L;

  private String dbSourceIdType;
  private Integer numOriginalInteractions;
  private Integer numUniqueOriginalInteractions;
  private Integer numUniqueOriginalGenes;
  private Integer numInteractionsNotToUniProtKB;
  private Integer numGenesNotToUniProtKB;
  private Integer numInteractionsNotToGeneId;
  private Integer numGenesNotToGeneId;
  private Integer numFinalInteractions;
  private Integer numFinalGenes;
  private Integer numRemovedInterSpeciesInteractions;
  private Integer numMultimappedToGeneId;
  
  public DatabaseInteractomeData(
    int id, String name, IdAndUri speciesA, IdAndUri speciesB, String dbSourceIdType, Integer numOriginalInteractions,
    Integer numUniqueOriginalInteractions, Integer numUniqueOriginalGenes,
    Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    String interactomeCollection
  ) {
    super(id, name, speciesA, speciesB, InteractomeType.DATABASE, interactomeCollection);

    this.dbSourceIdType = dbSourceIdType;
    this.numOriginalInteractions = numOriginalInteractions;
    this.numUniqueOriginalInteractions = numUniqueOriginalInteractions;
    this.numUniqueOriginalGenes = numUniqueOriginalGenes;
    this.numInteractionsNotToUniProtKB = numInteractionsNotToUniProtKB;
    this.numGenesNotToUniProtKB = numGenesNotToUniProtKB;
    this.numInteractionsNotToGeneId = numInteractionsNotToGeneId;
    this.numGenesNotToGeneId = numGenesNotToGeneId;
    this.numFinalInteractions = numFinalInteractions;
    this.numFinalGenes = numFinalGenes;
    this.numRemovedInterSpeciesInteractions = numRemovedInterSpeciesInteractions;
    this.numMultimappedToGeneId = numMultimappedToGeneId;
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

  public Integer getNumFinalGenes() {
    return numFinalGenes;
  }

  public void setNumFinalGenes(Integer numFinalGenes) {
    this.numFinalGenes = numFinalGenes;
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
}
