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

import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.spi.bio.GeneService;

@Stateless
@PermitAll
public class DefaultGeneService implements GeneService {
  @Inject
  private GeneDAO dao;
  
  @Inject
  private InteractomeDAO interactomeDAO;
  
  @Override
  public Gene get(int id) {
    return this.dao.getGene(id);
  }
  
  @Override
  public Stream<Gene> findByIdPrefixAndInteractome(int idPrefix, Set<Integer> interactomeIds, int maxResults) {
    if (interactomeIds.isEmpty()) {
      return this.dao.findByIdPrefix(idPrefix, maxResults);
    } else {
      return interactomeIds.stream()
        .map(interactomeId -> this.findByInteractome(idPrefix, interactomeId, maxResults))
        .reduce(Stream.empty(), Stream::concat)
        .distinct()
        .sorted((g1, g2) -> Integer.compare(g1.getId(), g2.getId()))
        .limit(maxResults);
    }
  }

  private Stream<Gene> findByInteractome(int idPrefix, int interactomeId, int maxResults) {
    final Interactome interactome = this.interactomeDAO.getInteractome(interactomeId);

    return Stream.concat(
      interactome.getInteractions().map(Interaction::getGeneA),
      interactome.getInteractions().map(Interaction::getGeneB)
    )
      .distinct()
    .filter(gene -> Integer.toString(gene.getId()).startsWith(Integer.toString(idPrefix)));
  }
}
