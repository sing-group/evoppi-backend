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
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.GENERATE_REFERENCE_FASTA_STEP_ID;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.storage.GeneStorageService;

@Transactional(REQUIRES_NEW)
public class DefaultDifferentSpeciesGeneInteractionsGenerateReferenceFastaStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private GeneStorageService geneStorageService;

  @Inject
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  @Override
  public String getStepId() {
    return GENERATE_REFERENCE_FASTA_STEP_ID;
  }

  @Override
  public String getName() {
    return "Reference FASTA creation";
  }
  
  @Override
  public int getOrder() {
    return 2;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getReferenceFastaPath()
      .map(path -> isRegularFile(path) && isReadable(path))
    .orElse(false);
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    try {
      final Set<Gene> referenceGenes = context.getReferenceGeneIds().orElseThrow(IllegalStateException::new)
        .distinct()
        .mapToObj(this.geneDao::getGene)
      .collect(toSet());
      
      final Path referenceFastaPath = this.geneStorageService.createFasta(referenceGenes);
      
      return this.contextBuilderFactory.createBuilderFor(context)
        .setReferenceFastaPath(referenceFastaPath)
      .build();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
