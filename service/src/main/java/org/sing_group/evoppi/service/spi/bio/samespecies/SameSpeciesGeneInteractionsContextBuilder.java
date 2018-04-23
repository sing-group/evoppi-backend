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
package org.sing_group.evoppi.service.spi.bio.samespecies;

import java.util.Collection;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.InteractionIds;

public interface SameSpeciesGeneInteractionsContextBuilder {
  public SameSpeciesGeneInteractionsContextBuilder setInteractions(int degree, Stream<InteractionIds> interactions);
  
  public default SameSpeciesGeneInteractionsContextBuilder setInteractions(int degree, Collection<InteractionIds> interactions) {
    return this.setInteractions(degree, interactions.stream());
  }
  
  public SameSpeciesGeneInteractionsContextBuilder setCompletedInteractions(Stream<InteractionIds> interactions);
  
  public default SameSpeciesGeneInteractionsContextBuilder setCompletedInteractions(Collection<InteractionIds> interactions) {
    return this.setCompletedInteractions(interactions.stream());
  }
  
  public SameSpeciesGeneInteractionsContext build();
}