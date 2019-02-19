/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.entities.bio.execution;

import static org.sing_group.fluent.checker.Checks.requireNonNegative;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BlastQueryOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "evalue", precision = 10, scale = 1)
  private double evalue;
  
  @Column(name = "maxTargetSeqs")
  private int maxTargetSeqs;
  
  @Column(name = "minimumIdentity", precision = 10, scale = 1)
  private double minimumIdentity;
  
  @Column(name = "minimumAlignmentLength")
  private int minimumAlignmentLength;
  
  BlastQueryOptions() {}

  public BlastQueryOptions(double evalue, int maxTargetSeqs, double minimumIdentity, int minimumAlignmentLength) {
    if (evalue < 0d)
      throw new IllegalArgumentException("evalue must be a double with a non negative value");
    if (maxTargetSeqs < 0 || maxTargetSeqs > 100)
      throw new IllegalArgumentException("maxTargetSeqs must be an integer in range [0, 100]");
    if (minimumIdentity < 0d || minimumIdentity > 1d)
      throw new IllegalArgumentException("minimumIdentity must be a double in range [0, 1]");
    requireNonNegative(minimumAlignmentLength, "minimumAlignmentLength must be a non negative value");
    
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
    BlastQueryOptions other = (BlastQueryOptions) obj;
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
