/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity.bio;

import java.util.stream.Stream;

public class DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeDataset extends InteractionsDataset {
  protected Stream<InteractionResultData> interactionsMaxDegree1() {
    return Stream.of(
      new InteractionResultData(100, "100", 101, "101", degrees(1, 1)),
      new InteractionResultData(100, "100", 102, "102", degrees(2, 1)),
      new InteractionResultData(100, "100", 105, "105", degrees(1, 1)),
      new InteractionResultData(100, "100", 106, "106", degrees(2, 1)),
      new InteractionResultData(100, "100", 109, "109", degrees(1, 1)),
      new InteractionResultData(100, "100", 112, "112", degrees(1, 1, 2, 1))
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree2() {
    return Stream.concat(
      interactionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(101, "101", 103, "103", degrees(1, 2, 3, -1)),
        new InteractionResultData(102, "102", 103, "103", degrees(2, 2, 4, -1)),
        new InteractionResultData(105, "105", 106, "106", degrees(2, 2, 4, -1)),
        new InteractionResultData(105, "105", 107, "107", degrees(1, 2, 3, -1, 4, -1)),
        new InteractionResultData(109, "109", 110, "110", degrees(1, 2, 2, -1, 3, -1, 4, -1)),
        new InteractionResultData(112, "112", 113, "113", degrees(1, 2, 2, 2, 3, -1, 4, -1))
      )
    );
  }

  protected Stream<InteractionResultData> interactionsMaxDegree3() {
    return Stream.concat(
      interactionsMaxDegree1(),
      Stream.of(
        new InteractionResultData(101, "101", 103, "103", degrees(1, 2, 3, -1)),
        new InteractionResultData(102, "102", 103, "103", degrees(2, 2, 4, -1)),
        new InteractionResultData(105, "105", 106, "106", degrees(2, 2, 4, -1)),
        new InteractionResultData(105, "105", 107, "107", degrees(1, 2, 2, 3, 3, -1, 4, -1)),
        new InteractionResultData(109, "109", 110, "110", degrees(1, 2, 2, -1, 3, -1, 4, -1)),
        new InteractionResultData(112, "112", 113, "113", degrees(1, 2, 2, 2, 3, -1, 4, -1)),

        new InteractionResultData(103, "103", 104, "104", degrees(1, 3, 2, 3, 3, -1, 4, -1)),
        new InteractionResultData(107, "107", 108, "108", degrees(1, 3, 3, -1, 4, -1)),
        new InteractionResultData(110, "110", 111, "111", degrees(1, 3, 2, -1, 3, -1, 4, -1)),
        new InteractionResultData(113, "113", 114, "114", degrees(1, 3, 2, 3, 3, -1, 4, -1))
      )
    );
  }
}
