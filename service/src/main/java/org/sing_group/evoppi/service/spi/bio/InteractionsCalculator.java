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
package org.sing_group.evoppi.service.spi.bio;

import java.util.Collection;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;

public interface InteractionsCalculator {

  public void calculateInteractions(
    Gene gene, Collection<Interactome> interactomes, int maxDegree, InteractionsCalculusCallback callback
  );
  
  public default void calculateInteractions(
    Gene gene, Collection<Interactome> interactomes, int maxDegree
  ) {
    this.calculateInteractions(gene, interactomes, maxDegree, new InteractionsCalculusCallback() {
      
      @Override
      public void degreeCalculusStarted(int degree) {}
      
      @Override
      public void degreeCalculusFinished(int degree, Stream<GeneInteraction> interactions) {}
      
      @Override
      public void calculusStarted() {}
      
      @Override
      public void calculusFinished() {}
    });
  }
  
}