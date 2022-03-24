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

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "different-species-interactions-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "different-species-interactions-data", description = "Information of an interaction between two genes.")
public class DifferentSpeciesInteractionsData extends InteractionsData {
  private static final long serialVersionUID = 1L;

  private BlastResultData[] blastResults;

  protected DifferentSpeciesInteractionsData() {}
  
  public DifferentSpeciesInteractionsData(
    UuidAndUri result,
    InteractionsResultFilteringOptionsData filteringOptions,
    BlastResultData[] blastResults,
    InteractionResultData[] interactions
  ) {
    super(result, filteringOptions, interactions);

    this.blastResults = blastResults;
  }

  public BlastResultData[] getBlastResults() {
    return blastResults;
  }

  public void setBlastResults(BlastResultData[] blastResults) {
    this.blastResults = blastResults;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(blastResults);
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
    DifferentSpeciesInteractionsData other = (DifferentSpeciesInteractionsData) obj;
    if (!Arrays.equals(blastResults, other.blastResults))
      return false;
    return true;
  }
}
