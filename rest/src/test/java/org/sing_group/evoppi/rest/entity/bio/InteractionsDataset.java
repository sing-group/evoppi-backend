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

public abstract class InteractionsDataset {
  public Stream<InteractionResultData> interactionsWithMaxDegree(int degree) {
    switch (degree) {
      case 1:
        return interactionsMaxDegree1();
      case 2:
        return interactionsMaxDegree2();
      case 3:
        return interactionsMaxDegree3();
      default:
        throw new IllegalArgumentException("Invalid degree: " + degree);
    }
  }

  protected abstract Stream<InteractionResultData> interactionsMaxDegree1();

  protected abstract Stream<InteractionResultData> interactionsMaxDegree2();

  protected abstract Stream<InteractionResultData> interactionsMaxDegree3();

  protected static Map<Integer, Integer> degrees(int interactome, int degree) {
    return singletonMap(interactome, degree);
  }

  protected static Map<Integer, Integer> degrees(
    int interactomeA, int degreeA,
    int interactomeB, int degreeB
  ) {
    final Map<Integer, Integer> degrees = new HashMap<>();

    degrees.put(interactomeA, degreeA);
    degrees.put(interactomeB, degreeB);

    return degrees;
  }

  protected static Map<Integer, Integer> degrees(
    int interactomeA, int degreeA,
    int interactomeB, int degreeB,
    int interactomeC, int degreeC
  ) {
    final Map<Integer, Integer> degrees = degrees(interactomeA, degreeA, interactomeB, degreeB);

    degrees.put(interactomeC, degreeC);

    return degrees;
  }

  protected static Map<Integer, Integer> degrees(
    int interactomeA, int degreeA,
    int interactomeB, int degreeB,
    int interactomeC, int degreeC,
    int interactomeD, int degreeD
  ) {
    final Map<Integer, Integer> degrees = degrees(interactomeA, degreeA, interactomeB, degreeB, interactomeC, degreeC);
    
    degrees.put(interactomeD, degreeD);

    return degrees;
  }
}
