/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

@XmlRootElement(name = "blast-query-options", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "blast-query-options", description = "Parameter values used for the BLAST alignment.")
public class BlastQueryOptionsData implements Serializable {
  private static final long serialVersionUID = 1L;

  private double evalue;
  private int maxTargetSeqs;
  private double minimumIdentity;
  private int minimumAlignmentLength;
  
  BlastQueryOptionsData() {}

  public BlastQueryOptionsData(double evalue, int maxTargetSeqs, double minimumIdentity, int minimumAlignmentLength) {
    this.evalue = evalue;
    this.maxTargetSeqs = maxTargetSeqs;
    this.minimumIdentity = minimumIdentity;
    this.minimumAlignmentLength = minimumAlignmentLength;
  }

  public double getEvalue() {
    return evalue;
  }

  public void setEvalue(double evalue) {
    this.evalue = evalue;
  }

  public int getMaxTargetSeqs() {
    return maxTargetSeqs;
  }

  public void setMaxTargetSeqs(int maxTargetSeqs) {
    this.maxTargetSeqs = maxTargetSeqs;
  }

  public double getMinimumIdentity() {
    return minimumIdentity;
  }

  public void setMinimumIdentity(double minimumIdentity) {
    this.minimumIdentity = minimumIdentity;
  }

  public int getMinimumAlignmentLength() {
    return minimumAlignmentLength;
  }

  public void setMinimumAlignmentLength(int minimumAlignmentLength) {
    this.minimumAlignmentLength = minimumAlignmentLength;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(evalue);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + maxTargetSeqs;
    result = prime * result + minimumAlignmentLength;
    temp = Double.doubleToLongBits(minimumIdentity);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    BlastQueryOptionsData other = (BlastQueryOptionsData) obj;
    if (Double.doubleToLongBits(evalue) != Double.doubleToLongBits(other.evalue))
      return false;
    if (maxTargetSeqs != other.maxTargetSeqs)
      return false;
    if (minimumAlignmentLength != other.minimumAlignmentLength)
      return false;
    if (Double.doubleToLongBits(minimumIdentity) != Double.doubleToLongBits(other.minimumIdentity))
      return false;
    return true;
  }
}
