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

package org.sing_group.evoppi.service.spi.bio.differentspecies;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;

public interface DifferentSpeciesGeneInteractionsContextBuilder {

  public default DifferentSpeciesGeneInteractionsContextBuilder addReferenceInteractions(
    int degree, Collection<HasGeneInteractionIds> interactions
  ) {
    return this.addReferenceInteractions(degree, interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder addReferenceInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  );
  
  public default DifferentSpeciesGeneInteractionsContextBuilder addReferenceCompletedInteractions(
    Collection<HasGeneInteractionIds> interactions
  ) {
    return this.addReferenceCompletedInteractions(interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder addReferenceCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  );

  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceFastaPath(Path referenceFastaPath);
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetFastaPath(Path targetFastaPath);

  public default DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Collection<BlastResult> blastResults) {
    return this.setBlastResults(blastResults.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Stream<BlastResult> blastResults);

  public default DifferentSpeciesGeneInteractionsContextBuilder addTargetInteractions(
    int degree, Collection<HasGeneInteractionIds> interactions
  ) {
    return this.addTargetInteractions(degree, interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder addTargetInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  );
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractionsCalculated();
  
  public default DifferentSpeciesGeneInteractionsContextBuilder addTargetCompletedInteractions(
    Collection<HasGeneInteractionIds> interactions
  ) {
    return this.addTargetCompletedInteractions(interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder addTargetCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  );
  
  public DifferentSpeciesGeneInteractionsContext build();

}
