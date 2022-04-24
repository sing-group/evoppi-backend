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

import java.util.stream.IntStream;

public interface HasGenePairIds {
  public int getGeneAId();

  public int getGeneBId();
  
  public default boolean hasGenes(HasGenePairIds genePairIds) {
    return this.hasGenes(genePairIds.getGeneAId(), genePairIds.getGeneBId());
  }
  
  public default boolean hasGenes(int geneAId, int geneBId) {
    return (getGeneAId() == geneAId && getGeneBId() == geneBId)
      || (getGeneBId() == geneAId && getGeneAId() == geneBId);
  }

  public default boolean hasGene(int geneId) {
    return getGeneAId() == geneId || getGeneBId() == geneId;
  }

  public default IntStream getGeneIds() {
    return IntStream.of(getGeneAId(), getGeneBId());
  }
  
  public static DefaultHasGenePairIds of(int geneAId, int geneBId) {
    return new DefaultHasGenePairIds(geneAId, geneBId);
  }
  
  public static DefaultHasGenePairIds of(HasGenePairIds genePairIds) {
    return new DefaultHasGenePairIds(genePairIds);
  }
  
  public static boolean haveSameGenes(HasGenePairIds genePairA, HasGenePairIds genePairB) {
    return genePairA.getGeneAId() == genePairB.getGeneAId()
      && genePairA.getGeneBId() == genePairB.getGeneBId();
  }
}
