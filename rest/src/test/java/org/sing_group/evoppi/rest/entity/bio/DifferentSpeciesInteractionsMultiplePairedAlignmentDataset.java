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

import java.util.stream.Stream;

public final class DifferentSpeciesInteractionsMultiplePairedAlignmentDataset extends InteractionsDataset {
  protected Stream<InteractionResultData> interactionsMaxDegree1() {
    return Stream.of(
      new InteractionResultData(100, 101, degrees(1, 1, 2, 1)),
      new InteractionResultData(100, 110, degrees(1, 1))
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree2() {
    return Stream.concat(
      interactionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(100, 111, degrees(2, 1)),
        new InteractionResultData(101, 102, degrees(1, 2, 2, 2)),
        new InteractionResultData(101, 111, degrees(1, 2)),
        new InteractionResultData(102, 111, degrees(2, 2)),
        new InteractionResultData(110, 111, degrees(1, 2))
      )
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree3() {
    return Stream.concat(
      interactionsMaxDegree2(),
      Stream.of(
        new InteractionResultData(101, 112, degrees(2, 2)),
        new InteractionResultData(102, 103, degrees(1, 3, 2, 3)),
        new InteractionResultData(102, 112, degrees(1, 3)),
        new InteractionResultData(103, 112, degrees(2, 3)),
        new InteractionResultData(111, 112, degrees(1, 3, 2, 2))
      )
    );
  }
}
