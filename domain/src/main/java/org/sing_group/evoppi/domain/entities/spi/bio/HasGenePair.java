/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Gene;

public interface HasGenePair extends HasGenePairIds {
  public Gene getGeneA();

  public Gene getGeneB();

  @Override
  public default int getGeneAId() {
    return getGeneA().getId();
  }
  
  @Override
  public default int getGeneBId() {
    return getGeneB().getId();
  }
  
  public default boolean hasGenes(HasGenePair genePair) {
    return this.hasGenes(genePair.getGeneA(), genePair.getGeneB());
  }
  
  public default boolean hasGenes(Gene geneA, Gene geneB) {
    return (getGeneA().equals(geneA) && getGeneB().equals(geneB))
      || (getGeneB().equals(geneA) && getGeneA().equals(geneB));
  }
  
  public default Stream<Gene> getGenes() {
    return Stream.of(getGeneA(), getGeneB());
  }
  
  public static HasGenePair of(Gene geneA, Gene geneB) {
    return new DefaultHasGenePair(geneA, geneB);
  }
  
  public static HasGenePair of(HasGenePair genePair) {
    return new DefaultHasGenePair(genePair);
  }
  
  public static HasGenePair from(HasGenePair genePair, IntFunction<Gene> geneMapper) {
    return new HasGenePair() {
      
      @Override
      public int getGeneAId() {
        return genePair.getGeneAId();
      }
      
      @Override
      public int getGeneBId() {
        return genePair.getGeneBId();
      }
      
      @Override
      public Gene getGeneA() {
        return geneMapper.apply(this.getGeneAId());
      }
      
      @Override
      public Gene getGeneB() {
        return geneMapper.apply(this.getGeneBId());
      }
      
      @Override
      public String toString() {
        return new StringBuilder()
          .append(this.getGeneAId())
          .append(" - ")
          .append(this.getGeneBId())
        .toString();
      }
    };
  }
}
