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

import java.util.stream.Stream;

public class DifferentSpeciesInteractionResourceMissingQueryGene1vs3InteractomeDataset extends InteractionsDataset {
  protected Stream<InteractionResultData> interactionsMaxDegree1() {
    return Stream.of(
      new InteractionResultData(100, 101, degrees(1, 1)),
      new InteractionResultData(100, 105, degrees(1, 1)),
      new InteractionResultData(100, 109, degrees(1, 1)),
      new InteractionResultData(100, 112, degrees(1, 1))
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree2() {
    return Stream.concat(
      interactionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(101, 103, degrees(1, 2, 3, -1)),
        new InteractionResultData(105, 107, degrees(1, 2, 3, -1)),
        new InteractionResultData(109, 110, degrees(1, 2, 3, -1)),
        new InteractionResultData(112, 113, degrees(1, 2, 3, -1))
      )
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree3() {
    return Stream.concat(
      interactionsMaxDegree2(),
      Stream.of(
        new InteractionResultData(103, 104, degrees(1, 3, 3, -1)),
        new InteractionResultData(107, 108, degrees(1, 3, 3, -1)),
        new InteractionResultData(110, 111, degrees(1, 3, 3, -1)),
        new InteractionResultData(113, 114, degrees(1, 3, 3, -1))
      )
    );
  }
}
