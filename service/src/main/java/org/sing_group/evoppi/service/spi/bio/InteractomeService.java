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

package org.sing_group.evoppi.service.spi.bio;

import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.execution.InteractomeCreationWork;
import org.sing_group.evoppi.service.bio.entity.InteractomeCreationData;

public interface InteractomeService {

  public Stream<Interactome> listInteractomes(ListingOptions<Interactome> interactomeListingOptions);

  public Interactome getInteractome(int id);

  public long count();
  
  public long count(ListingOptions<Interactome> interactomeListingOptions);

  public InteractomeCreationWork createInteractome(InteractomeCreationData data);
}
