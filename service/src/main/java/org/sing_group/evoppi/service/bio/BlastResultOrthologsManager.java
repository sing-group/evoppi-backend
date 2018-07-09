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

package org.sing_group.evoppi.service.bio;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.spi.bio.OrthologsManager;

public class BlastResultOrthologsManager implements OrthologsManager {
  private final Set<BlastResult> blastResults;
  
  public BlastResultOrthologsManager(Stream<BlastResult> blastResults) {
    requireNonNull(blastResults);
    
    this.blastResults = blastResults.collect(toSet());
  }
  
  public BlastResultOrthologsManager(Collection<BlastResult> blastResults) {
    requireNonNull(blastResults);
    
    this.blastResults = new HashSet<>(blastResults);
  }

  @Override
  public IntStream getOrthologsForReferenceGene(int geneId) {
    return this.blastResults.stream()
      .filter(blast -> blast.getQseqid() == geneId)
      .mapToInt(BlastResult::getSseqid)
      .distinct();
  }

  @Override
  public IntStream getOrthologsForTargetGene(int geneId) {
    return this.blastResults.stream()
      .filter(blast -> blast.getSseqid() == geneId)
      .mapToInt(BlastResult::getQseqid)
      .distinct();
  }

}
