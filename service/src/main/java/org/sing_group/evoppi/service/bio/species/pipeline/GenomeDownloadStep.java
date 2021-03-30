/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.bio.species.pipeline;

import static java.nio.file.Files.createTempFile;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.sing_group.evoppi.service.FileUtils.sanitize;

import java.io.File;
import java.net.URL;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.sing_group.evoppi.service.bio.entity.SpeciesCreationData;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SingleSpeciesCreationStep;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;

@Default
public class GenomeDownloadStep
  implements SingleSpeciesCreationStep {
  
  private static final int TIME_OUT = 90 * 1000;

  @Inject
  private SpeciesCreationContextBuilderFactory contextBuilderFactory;

  @Override
  public String getStepId() {
    return SpeciesCreationPipeline.SINGLE_GENOME_DOWNLOAD;
  }

  @Override
  public String getName() {
    return "Downlaod genome";
  }

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public boolean isComplete(SpeciesCreationContext context) {
    return context.getGenomeFile().isPresent();
  }

  @Transactional(REQUIRED)
  @Override
  public SpeciesCreationContext execute(SpeciesCreationContext context) {
    final SpeciesCreationData creationData = context.getConfiguration().getData();

    try {
      File tempFile = createTempFile(sanitize(creationData.getName()), "gbff.gz").toFile();

      FileUtils.copyURLToFile(new URL(creationData.getGbffGzipFileUrl()), tempFile, TIME_OUT, TIME_OUT);

      final SpeciesCreationContextBuilder builder = this.contextBuilderFactory.createBuilderFor(context);

      return builder.addGenomeFile(tempFile).build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
