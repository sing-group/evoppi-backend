/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2021 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.domain.interactome;

import java.util.HashSet;
import java.util.stream.IntStream;

public class GeneInteractions extends HashSet<GeneInteraction> {
  private static final long serialVersionUID = 1L;

  public IntStream getGenes() {
    return this.stream().map(s -> new int[] {
      s.getA(), s.getB()
    }).flatMapToInt(IntStream::of).distinct();
  }

  public IntStream getAGenes() {
    return this.stream().map(s -> s.getA()).flatMapToInt(IntStream::of).distinct();
  }

  public IntStream getBGenes() {
    return this.stream().map(s -> s.getB()).flatMapToInt(IntStream::of).distinct();
  }
}
