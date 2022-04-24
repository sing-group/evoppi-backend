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
package org.sing_group.evoppi.service.bio.species;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEventManager;
import org.sing_group.seda.datatype.SequencesGroup;

public class DefaultSpeciesCreationContext implements SpeciesCreationContext, Serializable {
  private static final long serialVersionUID = 1L;

  private transient final SpeciesCreationPipeline pipeline;
  private final SpeciesCreationConfiguration configuration;
  private transient final SpeciesCreationEventManager eventManager;

  private final File genomeFile;
  private final Integer taxonomyId;
  private final SequencesGroup fasta;
  private final Map<Integer, List<String>> dictionary;

  DefaultSpeciesCreationContext(
    SpeciesCreationPipeline pipeline,
    SpeciesCreationConfiguration configuration,
    SpeciesCreationEventManager eventManager,
    File genomeFile,
    Integer taxonomyId,
    SequencesGroup fasta,
    Map<Integer, List<String>> dictionary
  ) {
    this.configuration = requireNonNull(configuration);
    this.eventManager = requireNonNull(eventManager);
    this.pipeline = requireNonNull(pipeline);
    this.genomeFile = genomeFile;
    this.taxonomyId = taxonomyId;
    this.fasta = fasta;
    this.dictionary = dictionary;
  }

  @Override
  public SpeciesCreationConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public SpeciesCreationEventManager getEventManager() {
    return this.eventManager;
  }

  @Override
  public SpeciesCreationPipeline getPipeline() {
    return this.pipeline;
  }

  @Override
  public Optional<File> getGenomeFile() {
    return Optional.ofNullable(this.genomeFile);
  }

  @Override
  public Optional<Integer> getTaxonomyId() {
    return Optional.ofNullable(this.taxonomyId);
  }

  @Override
  public Optional<SequencesGroup> getFasta() {
    return Optional.ofNullable(this.fasta);
  }

  @Override
  public Optional<Map<Integer, List<String>>> getDictionary() {
    return Optional.ofNullable(this.dictionary);
  }
}
