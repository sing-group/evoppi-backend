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
package org.sing_group.evoppi.service.bio.differentspecies;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;

public class DefaultDifferentSpeciesGeneInteractionsConfiguration
implements DifferentSpeciesGeneInteractionsConfiguration {
  private final int geneId;
  private final Set<Integer> referenceInteractomes;
  private final Set<Integer> targetInteractomes;
  private final BlastQueryOptions blastQueryOptions;
  private final int maxDegree;
  private final String workId;

  public DefaultDifferentSpeciesGeneInteractionsConfiguration(
    int geneId, Set<Integer> referenceInteractomes, Set<Integer> targetInteractomes,
    BlastQueryOptions blastQueryOptions, int maxDegree,
    String workId
  ) {
    this.geneId = geneId;
    this.referenceInteractomes = new HashSet<>(referenceInteractomes);
    this.targetInteractomes = new HashSet<>(targetInteractomes);
    this.blastQueryOptions = blastQueryOptions;
    this.maxDegree = maxDegree;
    this.workId = workId;
  }

  @Override
  public int getGeneId() {
    return geneId;
  }

  @Override
  public IntStream getReferenceInteractomes() {
    return referenceInteractomes.stream().mapToInt(Integer::intValue);
  }

  @Override
  public IntStream getTargetInteractomes() {
    return targetInteractomes.stream().mapToInt(Integer::intValue);
  }

  @Override
  public BlastQueryOptions getBlastQueryOptions() {
    return blastQueryOptions;
  }

  @Override
  public int getMaxDegree() {
    return maxDegree;
  }

  @Override
  public String getWorkId() {
    return workId;
  }
}
