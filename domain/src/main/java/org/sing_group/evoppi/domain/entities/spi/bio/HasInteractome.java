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

package org.sing_group.evoppi.domain.entities.spi.bio;

import java.util.function.IntFunction;

import org.sing_group.evoppi.domain.entities.bio.Interactome;

public interface HasInteractome extends HasInteractomeId {
  public Interactome getInteractome();
  
  public default boolean hasInteractome(HasInteractome interactome) {
    return this.hasInteractome(interactome.getInteractome());
  }
  
  public default boolean hasInteractome(Interactome interactome) {
    return this.getInteractome().equals(interactome);
  }
  
  @Override
  public default int getInteractomeId() {
    return getInteractome().getId();
  }
  
  public static HasInteractome of(Interactome interactome) {
    return new DefaultHasInteractome(interactome);
  }
  
  public static HasInteractome of(HasInteractome interactome) {
    return new DefaultHasInteractome(interactome);
  }
  
  public static HasInteractome from(HasInteractomeId hasInteractomeId, IntFunction<Interactome> interactomeMapper) {
    return new HasInteractome() {
      @Override
      public int getInteractomeId() {
        return hasInteractomeId.getInteractomeId();
      }
      
      @Override
      public Interactome getInteractome() {
        return interactomeMapper.apply(this.getInteractomeId());
      }
      
      @Override
      public String toString() {
        return Integer.toString(this.getInteractomeId());
      }
    };
  }
}
