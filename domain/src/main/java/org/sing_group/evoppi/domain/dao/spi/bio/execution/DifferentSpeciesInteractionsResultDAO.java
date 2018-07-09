/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.dao.spi.bio.execution;

import java.util.Collection;
import java.util.function.Function;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.user.User;

public interface DifferentSpeciesInteractionsResultDAO {

  public boolean exists(String id);

  public DifferentSpeciesInteractionsResult get(String interactionResultId);

  public DifferentSpeciesInteractionsResult create(
    String name,
    String description,
    String resultReference,
    Gene queryGene,
    Collection<Interactome> referenceInteractomes,
    Collection<Interactome> targetInteractomes,
    BlastQueryOptions blastOptions,
    int queryMaxDegree,
    User owner
  );

  public DifferentSpeciesInteractionsResult create(
    String name,
    String description,
    Function<String, String> resultReferenceBuilder,
    Gene queryGene,
    Collection<Interactome> referenceInteractomes,
    Collection<Interactome> targetInteractomes,
    BlastQueryOptions blastOptions,
    int queryMaxDegree,
    User owner
  );

}
