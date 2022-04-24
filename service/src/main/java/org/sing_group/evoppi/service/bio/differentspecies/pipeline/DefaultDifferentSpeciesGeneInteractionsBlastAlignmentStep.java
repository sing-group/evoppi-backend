/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.BLAST_ALIGNMENT_STEP_ID;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.spi.bio.BlastService;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;

@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesGeneInteractionsBlastAlignmentStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  @Inject
  private BlastService blastService;

  @Inject
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  @Override
  public String getStepId() {
    return BLAST_ALIGNMENT_STEP_ID;
  }
  
  @Override
  public String getName() {
    return "Blast alignment";
  }
  
  @Override
  public int getOrder() {
    return 4;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getBlastResults().isPresent();
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    final Path referenceFastaPath = context.getReferenceFastaPath().orElseThrow(IllegalStateException::new);
    final Path targetFastaPath = context.getTargetFastaPath().orElseThrow(IllegalStateException::new);
    
    try {
      final Stream<BlastResult> blastResults = this.blastService.blast(
        targetFastaPath,
        referenceFastaPath,
        context.getConfiguration().getBlastQueryOptions()
      );
      
      return this.contextBuilderFactory.createBuilderFor(context)
        .setBlastResults(blastResults)
      .build();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
