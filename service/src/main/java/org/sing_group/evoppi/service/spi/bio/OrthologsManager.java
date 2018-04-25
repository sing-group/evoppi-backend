/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.spi.bio;

import static java.util.function.Function.identity;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface OrthologsManager {
  public IntStream getOrthologsForReferenceGene(int geneId);

  public IntStream getOrthologsForTargetGene(int geneId);
  
  public default void forEachReferencePairOrthologs(int geneA, int geneB, BiConsumer<Integer, Integer> action) {
    getOrthologsForReferenceGene(geneA)
      .forEach(orthologA -> getOrthologsForReferenceGene(geneB)
        .forEach(orthologB -> action.accept(orthologA, orthologB))
      );
  }
  
  public default void forEachTargetPairOrthologs(int geneA, int geneB, BiConsumer<Integer, Integer> action) {
    getOrthologsForTargetGene(geneA)
      .forEach(orthologA -> getOrthologsForTargetGene(geneB)
        .forEach(orthologB -> action.accept(orthologA, orthologB))
      );
  }
  
  public default <T> Stream<T> mapReferencePairOrthologs(int geneA, int geneB, BiFunction<Integer, Integer, T> action) {
    return getOrthologsForReferenceGene(geneA)
      .mapToObj(orthologA -> getOrthologsForReferenceGene(geneB)
        .mapToObj(orthologB -> action.apply(orthologA, orthologB))
        )
      .flatMap(identity());
  }
  
  public default <T> Stream<T> mapTargetPairOrthologs(int geneA, int geneB, BiFunction<Integer, Integer, T> action) {
    return getOrthologsForTargetGene(geneA)
      .mapToObj(orthologA -> getOrthologsForTargetGene(geneB)
        .mapToObj(orthologB -> action.apply(orthologA, orthologB))
        )
      .flatMap(identity());
  }
}
