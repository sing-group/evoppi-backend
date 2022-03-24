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

import org.sing_group.evoppi.rest.entity.UuidAndUri;

public abstract class InteractionsData implements Serializable {
  private static final long serialVersionUID = 1L;

  private UuidAndUri result;
  
  private InteractionsResultFilteringOptionsData filteringOptions;
  
  private InteractionResultData[] interactions;
  
  protected InteractionsData() {}

  public InteractionsData(
    UuidAndUri result,
    InteractionsResultFilteringOptionsData filteringOptions,
    InteractionResultData[] interactions
  ) {
    this.result = result;
    this.filteringOptions = filteringOptions;
    this.interactions = interactions;
  }

  public UuidAndUri getResult() {
    return result;
  }

  public void setResult(UuidAndUri result) {
    this.result = result;
  }

  public InteractionsResultFilteringOptionsData getFilteringOptions() {
    return filteringOptions;
  }

  public void setFilteringOptions(InteractionsResultFilteringOptionsData filteringOptions) {
    this.filteringOptions = filteringOptions;
  }

  public InteractionResultData[] getInteractions() {
    return interactions;
  }

  public void setInteractions(InteractionResultData[] interactions) {
    this.interactions = interactions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((filteringOptions == null) ? 0 : filteringOptions.hashCode());
    result = prime * result + Arrays.hashCode(interactions);
    result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
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
    InteractionsData other = (InteractionsData) obj;
    if (filteringOptions == null) {
      if (other.filteringOptions != null)
        return false;
    } else if (!filteringOptions.equals(other.filteringOptions))
      return false;
    if (!Arrays.equals(interactions, other.interactions))
      return false;
    if (result == null) {
      if (other.result != null)
        return false;
    } else if (!result.equals(other.result))
      return false;
    return true;
  }
}
