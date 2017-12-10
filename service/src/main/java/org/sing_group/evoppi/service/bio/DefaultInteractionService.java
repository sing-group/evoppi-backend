/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Stateless
@PermitAll
public class DefaultInteractionService implements InteractionService {
  @Inject
  private GeneDAO geneDao;

  @Override
  public Stream<Interaction> findInteractionsByGene(int geneId, int[] interactomes) {
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    final Gene gene = this.geneDao.getGene(geneId);
    
    final Set<Integer> interactomesIds = IntStream.of(interactomes)
      .boxed()
    .collect(Collectors.toSet());
    
    return gene.getInteractsWith()
      .filter(interaction -> interactomesIds.contains(interaction.getInteractome().getId()));
  }

}
