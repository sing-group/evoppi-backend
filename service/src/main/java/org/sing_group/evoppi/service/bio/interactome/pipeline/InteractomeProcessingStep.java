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
package org.sing_group.evoppi.service.bio.interactome.pipeline;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.sing_group.evoppi.service.FileUtils.sanitize;
import static org.sing_group.interactomesparser.InteractomeFileFormat.DEFAULT_FIELD_DELIMITER;
import static org.sing_group.interactomesparser.InteractomeFileFormat.DEFAULT_FIELD_ORGANISM_1_COLUMN;
import static org.sing_group.interactomesparser.InteractomeFileFormat.DEFAULT_FIELD_ORGANISM_2_COLUMN;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteraction;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;
import org.sing_group.evoppi.service.bio.entity.DatabaseInteractomeCreationData;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.SingleInteractomeCreationStep;
import org.sing_group.interactomesparser.InteractomeFileFormat;
import org.sing_group.interactomesparser.InteractomeFileFormatException;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessing;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessingStatistics;

@Default
public class InteractomeProcessingStep
  implements SingleInteractomeCreationStep {

  @Inject
  private SpeciesDAO speciesDao;

  @Inject
  private DatabaseInteractomeCreationContextBuilderFactory contextBuilderFactory;

  @Override
  public String getStepId() {
    return DatabaseInteractomeCreationPipeline.SINGLE_PROCESS_INTERACTOME;
  }

  @Override
  public String getName() {
    return "Process interactome";
  }

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public boolean isComplete(DatabaseInteractomeCreationContext context) {
    return context.getStatistics().isPresent() && context.getInteractions().isPresent();
  }

  @Transactional(REQUIRED)
  @Override
  public DatabaseInteractomeCreationContext execute(DatabaseInteractomeCreationContext context) {
    final DatabaseInteractomeCreationData creationData = context.getConfiguration().getData();
    final Species species = this.speciesDao.getSpecies(creationData.getSpeciesDbId());

    final EvoPpiInteractomeProcessing processing =
      new EvoPpiInteractomeProcessing(
        creationData.getFile(),
        toInteractomeFileFormat(creationData, species)
      );

    File temporaryDir;
    String interactomeFileName = sanitize(creationData.getName());
    try {
      temporaryDir = Files.createTempDirectory("evoppi_tmp_" + interactomeFileName).toFile();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Error creating temporary directory");
    }
    File temporaryInteractome = new File(temporaryDir, interactomeFileName + ".tsv");

    try {
      EvoPpiInteractomeProcessingStatistics stats =
        processing.process(species.getName(), temporaryInteractome, temporaryDir);

      GeneInteractions interactions = parseInteractions(temporaryInteractome);

      final DatabaseInteractomeCreationContextBuilder builder = this.contextBuilderFactory.createBuilderFor(context);

      return builder
        .addStatistics(stats)
        .addInteractions(interactions)
        .build();
    } catch (IOException | InteractomeFileFormatException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static InteractomeFileFormat toInteractomeFileFormat(DatabaseInteractomeCreationData data, Species species) {
    System.err.println(data.toString());
    return new InteractomeFileFormat(
      data.getDbSource().getName(),
      data.getGeneColumn1(),
      data.getGeneColumn2(),
      data.getHeaderLinesCount() == null ? 0 : data.getHeaderLinesCount(),
      toSpeciesMetadata(species.getName(), species.getTaxonomyId()),
      data.getGenePrefix() == null ? "" : data.getGenePrefix(),
      data.getGeneSuffix() == null ? "" : data.getGeneSuffix(),
      data.getOrganismColumn1() == null ? DEFAULT_FIELD_ORGANISM_1_COLUMN : data.getOrganismColumn1(),
      data.getOrganismColumn2() == null ? DEFAULT_FIELD_ORGANISM_2_COLUMN : data.getOrganismColumn2(),
      data.getOrganismPrefix() == null ? "" : data.getOrganismPrefix(),
      data.getOrganismSuffix() == null ? "" : data.getOrganismSuffix(),
      DEFAULT_FIELD_DELIMITER
    );
  }

  private static String toSpeciesMetadata(String species, Integer id) {
    if (id == null) {
      return species;
    } else {
      return species + ":id=" + id;
    }
  }

  private static GeneInteractions parseInteractions(File interactomeFile) throws IOException {
    GeneInteractions interactions = new GeneInteractions();

    Files.readAllLines(interactomeFile.toPath()).forEach(l -> {
      String[] split = l.split(InteractomeFileFormat.DEFAULT_FIELD_DELIMITER);
      if (split.length == 2) {
        try {
          Integer first = Integer.valueOf(split[0]);
          Integer second = Integer.valueOf(split[1]);
          if (first < second) {
            interactions.add(new GeneInteraction(first, second));
          } else {
            interactions.add(new GeneInteraction(second, first));
          }
          ;
        } catch (NumberFormatException e) {
          throw new RuntimeException(e);
        }
      } else {
        System.err.println("Warning: ignoring interactome line without two columns");
      }
    });

    return interactions;
  }
}
