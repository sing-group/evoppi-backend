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
package org.sing_group.evoppi.domain.dao.spi.bio;

import java.util.Collection;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;

public interface GeneInInteractomeDAO {
  public GeneInInteractome create(Species species, Interactome interactome, Gene gene);

  public void removeGeneInInteractomeBySpecies(int speciesId);

  public void removeGeneInInteractomeByInteractomeIds(Collection<Integer> interactomeId);
}
