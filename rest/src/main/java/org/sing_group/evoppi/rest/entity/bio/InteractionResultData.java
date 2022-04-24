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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "interaction-result-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "interaction-result-data", description = "Information of an interaction between two genes.")
public class InteractionResultData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "geneA", required = true)
  private int geneA;
  
  @XmlElement(name = "geneAName", required = true)
  private String geneAName;

  @XmlElement(name = "geneB", required = true)
  private int geneB;
  
  @XmlElement(name = "geneBName", required = true)
  private String geneBName;

  @XmlJavaTypeAdapter(InteractomeDegreeAdapter.class)
  private Map<Integer, Integer> interactomeDegrees;
  
  InteractionResultData() {}

  public InteractionResultData(
    int geneA, String geneAName, int geneB, String geneBName, Map<Integer, Integer> interactomeDegrees
  ) {
    this.geneA = geneA;
    this.geneAName = geneAName;
    this.geneB = geneB;
    this.geneBName = geneBName;
    this.interactomeDegrees = new HashMap<>(interactomeDegrees);
  }

  public int getGeneA() {
    return geneA;
  }

  public void setGeneA(int geneA) {
    this.geneA = geneA;
  }
  
  public String getGeneAName() {
    return geneAName;
  }
  
  public void setGeneAName(String geneAName) {
    this.geneAName = geneAName;
  }

  public int getGeneB() {
    return geneB;
  }

  public void setGeneB(int geneB) {
    this.geneB = geneB;
  }
  
  public String getGeneBName() {
    return geneBName;
  }

  public void setGeneBName(String geneBName) {
    this.geneBName = geneBName;
  }

  public IntStream getGenes() {
    return IntStream.of(this.geneA, this.geneB);
  }
  
  public Map<Integer, Integer> getInteractomeDegrees() {
    return interactomeDegrees;
  }

  public void setInteractomeDegrees(Map<Integer, Integer> interactomeDegrees) {
    this.interactomeDegrees = interactomeDegrees;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + geneA;
    result = prime * result + geneB;
    result = prime * result + ((interactomeDegrees == null) ? 0 : interactomeDegrees.hashCode());
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
    InteractionResultData other = (InteractionResultData) obj;
    if (geneA != other.geneA)
      return false;
    if (geneB != other.geneB)
      return false;
    if (interactomeDegrees == null) {
      if (other.interactomeDegrees != null)
        return false;
    } else if (!interactomeDegrees.equals(other.interactomeDegrees))
      return false;
    return true;
  }

}
