/*-
 * #%L
 * Service
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

package org.sing_group.evoppi.service.bio;

import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePair;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;

@Default
public class DefaultInteractionsCalculator implements InteractionsCalculator {
  @Transactional(MANDATORY)
  @Override
  public void calculateInteractions(
    Gene initialGene, int interactomeId, int maxDegree, InteractionsCalculusCallback callback
  ) {
    callback.calculusStarted();
    
    final Set<HasGenePair> interactions = new HashSet<>();

    final Set<Gene> closedGenes = new HashSet<>();
    Set<Gene> openGenes = new HashSet<>();
    openGenes.add(initialGene);
    
    for (int degree = 1; degree <= maxDegree; degree++) {
      final int currentDegree = degree;
      
      final Set<HasGenePair> newInteractions = openGenes.stream()
        .flatMap(gene -> gene.getInteractionsOfInteractome(interactomeId))
        .map(HasGenePair::of)
      .collect(toSet());
      
      newInteractions.removeAll(interactions);
      
      interactions.addAll(newInteractions);
      
      callback.interactionsCalculated(currentDegree, newInteractions);
      
      closedGenes.addAll(openGenes);
      
      if (degree < maxDegree) { // ignored in the last iteration as they will not be needed
        openGenes = newInteractions.stream()
          .flatMap(HasGenePair::getGenes)
          .filter(gene -> !closedGenes.contains(gene))
        .collect(toSet());
      }
    }
    
    callback.calculusFinished();
  }
}
