/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.spi.storage;

import java.io.Serializable;

public class FastaOutputConfiguration implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean includeVersionSuffix;
  private boolean sorted;

  public FastaOutputConfiguration() {
    this(true, false);
  }

  public FastaOutputConfiguration(boolean includeVersionSuffix, boolean sorted) {
    this.includeVersionSuffix = includeVersionSuffix;
    this.sorted = sorted;
  }

  public boolean isIncludeVersionSuffix() {
    return includeVersionSuffix;
  }

  public void setIncludeVersionSuffix(boolean includeVersionSuffix) {
    this.includeVersionSuffix = includeVersionSuffix;
  }

  public boolean isSorted() {
    return sorted;
  }

  public void setSorted(boolean sorted) {
    this.sorted = sorted;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (includeVersionSuffix ? 1231 : 1237);
    result = prime * result + (sorted ? 1231 : 1237);
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
    FastaOutputConfiguration other = (FastaOutputConfiguration) obj;
    if (includeVersionSuffix != other.includeVersionSuffix)
      return false;
    if (sorted != other.sorted)
      return false;
    return true;
  }

}
