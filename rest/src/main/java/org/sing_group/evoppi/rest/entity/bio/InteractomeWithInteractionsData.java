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
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "interactome-data-with-interactions", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "interactome-data-with-interactions", description = "Data of an interactome entity with its interactions."
)
public class InteractomeWithInteractionsData extends InteractomeData {
  private static final long serialVersionUID = 1L;

  @XmlElementWrapper(name = "genes", required = true)
  @XmlElement(name = "genes", required = true)
  private IdAndUri[] genes;

  @XmlElementWrapper(name = "interactions", required = true)
  @XmlElement(name = "interaction", required = true)
  private InteractingGenes[] interactions;

  public InteractomeWithInteractionsData(
    int id, String name, IdAndUri species, String dbSourceIdType, Integer numOriginalInteractions,
    Integer numUniqueOriginalInteractions, Integer numUniqueOriginalGenes,
    Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    IdAndUri[] genes, InteractingGenes[] interactions
  ) {
    super(
      id, name, species, dbSourceIdType, numOriginalInteractions, numUniqueOriginalInteractions,
      numUniqueOriginalGenes, numInteractionsNotToUniProtKB, numGenesNotToUniProtKB, numInteractionsNotToGeneId,
      numGenesNotToGeneId, numFinalInteractions, numFinalGenes, numRemovedInterSpeciesInteractions,
      numMultimappedToGeneId
    );

    this.genes = genes;
    this.interactions = interactions;
  }

  public IdAndUri[] getGenes() {
    return genes;
  }

  public void setGenes(IdAndUri[] genes) {
    this.genes = genes;
  }

  public InteractingGenes[] getInteractions() {
    return interactions;
  }

  public void setInteractions(InteractingGenes[] interactions) {
    this.interactions = interactions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(genes);
    result = prime * result + Arrays.hashCode(interactions);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    InteractomeWithInteractionsData other = (InteractomeWithInteractionsData) obj;
    if (!Arrays.equals(genes, other.genes))
      return false;
    if (!Arrays.equals(interactions, other.interactions))
      return false;
    return true;
  }

  @XmlRootElement(name = "interacting-genes", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
  @XmlAccessorType(XmlAccessType.FIELD)
  @ApiModel(value = "interacting-genes", description = "Basic information of an interaction between two genes.")
  public static class InteractingGenes implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "geneA", required = true)
    private long geneA;

    @XmlAttribute(name = "geneB", required = true)
    private long geneB;

    InteractingGenes() {}

    public InteractingGenes(long geneA, long geneB) {
      this.geneA = geneA;
      this.geneB = geneB;
    }

    public long getGeneA() {
      return geneA;
    }

    public void setGeneA(long geneA) {
      this.geneA = geneA;
    }

    public long getGeneB() {
      return geneB;
    }

    public void setGeneB(long geneB) {
      this.geneB = geneB;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (geneA ^ (geneA >>> 32));
      result = prime * result + (int) (geneB ^ (geneB >>> 32));
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
      InteractingGenes other = (InteractingGenes) obj;
      if (geneA != other.geneA)
        return false;
      if (geneB != other.geneB)
        return false;
      return true;
    }
  }
}
