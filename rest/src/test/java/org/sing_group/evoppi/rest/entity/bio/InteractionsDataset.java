/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.bio;

import static java.util.Collections.singletonMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class InteractionsDataset {
  private InteractionsDataset() {}

  public static Stream<InteractionResultData> sameSpeciesInteractionsWithMaxDegree(int degree) {
    switch (degree) {
      case 1:
        return sameSpeciesInteractionsMaxDegree1();
      case 2:
        return sameSpeciesInteractionsMaxDegree2();
      case 3:
        return sameSpeciesInteractionsMaxDegree3();
      default:
        throw new IllegalArgumentException("Invalid degree: " + degree);
    }

  }

  public static Stream<InteractionResultData> sameSpeciesInteractionsMaxDegree1() {
    return Stream.of(
      new InteractionResultData(100, 101, degrees(1, 1)),
      new InteractionResultData(100, 102, degrees(2, 1)),
      new InteractionResultData(100, 105, degrees(1, 1)),
      new InteractionResultData(100, 106, degrees(2, 1)),
      new InteractionResultData(100, 109, degrees(1, 1)),
      new InteractionResultData(100, 112, degrees(1, 1, 2, 1))
    );
  }

  public static Stream<InteractionResultData> sameSpeciesInteractionsMaxDegree2() {
    return Stream.concat(
      sameSpeciesInteractionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(101, 103, degrees(1, 2)),
        new InteractionResultData(102, 103, degrees(2, 2)),
        new InteractionResultData(105, 106, degrees(2, 2)),
        new InteractionResultData(105, 107, degrees(1, 2)),
        new InteractionResultData(109, 110, degrees(1, 2, 2, -1)),
        new InteractionResultData(112, 113, degrees(1, 2, 2, 2))
      )
    );
  }

  public static Stream<InteractionResultData> sameSpeciesInteractionsMaxDegree3() {
    return Stream.concat(
      sameSpeciesInteractionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(101, 103, degrees(1, 2)),
        new InteractionResultData(102, 103, degrees(2, 2)),
        new InteractionResultData(105, 106, degrees(2, 2)),
        new InteractionResultData(105, 107, degrees(1, 2, 2, 3)),
        new InteractionResultData(109, 110, degrees(1, 2, 2, -1)),
        new InteractionResultData(112, 113, degrees(1, 2, 2, 2)),

        new InteractionResultData(103, 104, degrees(1, 3, 2, 3)),
        new InteractionResultData(107, 108, degrees(1, 3)),
        new InteractionResultData(110, 111, degrees(1, 3, 2, -1)),
        new InteractionResultData(113, 114, degrees(1, 3, 2, 3))
      )
    );
  }

  private static Map<Integer, Integer> degrees(int interactome, int degree) {
    return singletonMap(interactome, degree);
  }

  private static Map<Integer, Integer> degrees(
    int interactomeA, int degreeA,
    int interactomeB, int degreeB
  ) {
    final Map<Integer, Integer> degrees = new HashMap<>();

    degrees.put(interactomeA, degreeA);
    degrees.put(interactomeB, degreeB);

    return degrees;
  }
}
