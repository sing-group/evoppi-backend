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
package org.sing_group.evoppi.service.spi.bio.differentspecies;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;

public interface DifferentSpeciesGeneInteractionsContextBuilder {

  public default DifferentSpeciesGeneInteractionsContextBuilder setReferenceInteractions(
    int degree, Collection<HasGeneInteractionIds> interactions
  ) {
    return this.setReferenceInteractions(degree, interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  );
  
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceInteractions(
    Map<Integer, Set<HasGeneInteractionIds>> referenceInteractions
  );
  
  public default DifferentSpeciesGeneInteractionsContextBuilder setReferenceCompletedInteractions(
    Collection<HasGeneInteractionIds> interactions
  ) {
    return this.setReferenceCompletedInteractions(interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  );

  public DifferentSpeciesGeneInteractionsContextBuilder setReferenceFastaPath(Path referenceFastaPath);
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetFastaPath(Path targetFastaPath);

  public default DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Collection<BlastResult> blastResults) {
    return this.setBlastResults(blastResults.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setBlastResults(Stream<BlastResult> blastResults);

  public default DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractions(
    int degree, Collection<HasGeneInteractionIds> interactions
  ) {
    return this.setTargetInteractions(degree, interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractions(
    int degree, Stream<HasGeneInteractionIds> interactions
  );
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetInteractions(
    Map<Integer, Set<HasGeneInteractionIds>> targetInteractions
  );
  
  public default DifferentSpeciesGeneInteractionsContextBuilder setTargetCompletedInteractions(
    Collection<HasGeneInteractionIds> interactions
  ) {
    return this.setTargetCompletedInteractions(interactions.stream());
  }
  
  public DifferentSpeciesGeneInteractionsContextBuilder setTargetCompletedInteractions(
    Stream<HasGeneInteractionIds> interactions
  );
  
  public DifferentSpeciesGeneInteractionsContext build();

}
