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

package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isRegularFile;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.GENERATE_TARGET_FASTA_STEP_ID;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.storage.GenomeStorageService;

@Transactional(REQUIRES_NEW)
public class DefaultDifferentSpeciesGeneInteractionsGenerateTargetFastaStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private GenomeStorageService genomeStorageService;

  @Inject
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  @Override
  public String getStepId() {
    return GENERATE_TARGET_FASTA_STEP_ID;
  }

  @Override
  public String getName() {
    return "Target FASTA creation";
  }
  
  @Override
  public int getOrder() {
    return 3;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getTargetFastaPath()
      .map(path -> isRegularFile(path) && isReadable(path))
    .orElse(false);
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    try {
      final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
      
      final Interactome targetInteractome = configuration.getTargetInteractomes()
        .mapToObj(this.interactomeDao::getInteractome)
        .findAny()
        .orElseThrow(IllegalStateException::new);
      
      final Path targetFastaPath = this.genomeStorageService.getGenomePath(targetInteractome.getSpecies());
      
      return this.contextBuilderFactory.createBuilderFor(context)
        .setTargetFastaPath(targetFastaPath)
      .build();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
