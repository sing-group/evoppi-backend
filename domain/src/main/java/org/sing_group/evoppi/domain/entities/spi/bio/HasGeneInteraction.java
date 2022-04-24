/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.spi.bio;

import java.util.function.IntFunction;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

public interface HasGeneInteraction extends HasInteractome, HasGenePair {
  public static HasGeneInteraction of(Interactome interactome, HasGenePair genePair) {
    return new DefaultHasGeneInteraction(interactome, genePair);
  }

  public static HasGeneInteraction of(HasGeneInteraction interaction) {
    return new DefaultHasGeneInteraction(interaction);
  }
  
  public static HasGeneInteraction from(
    HasGeneInteractionIds hasInteractionIds,
    IntFunction<Gene> geneMapper,
    IntFunction<Interactome> interactomeMapper
  ) {
    return new HasGeneInteraction() {
      @Override
      public int getGeneAId() {
        return hasInteractionIds.getGeneAId();
      }
      
      @Override
      public Gene getGeneA() {
        return geneMapper.apply(this.getGeneAId());
      }
      
      @Override
      public int getGeneBId() {
        return hasInteractionIds.getGeneBId();
      }
      
      @Override
      public Gene getGeneB() {
        return geneMapper.apply(this.getGeneBId());
      }
      
      @Override
      public int getInteractomeId() {
        return hasInteractionIds.getInteractomeId();
      }
      
      @Override
      public Interactome getInteractome() {
        return interactomeMapper.apply(this.getInteractomeId());
      }
      
      @Override
      public String toString() {
        return new StringBuilder()
          .append(this.getInteractomeId())
          .append(": ")
          .append(this.getGeneAId())
          .append(" - ")
          .append(this.getGeneBId())
        .toString();
      }
    };
  }
}
