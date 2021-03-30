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

package org.sing_group.evoppi.service.bio.species;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEventManager;
import org.sing_group.seda.datatype.SequencesGroup;

public class DefaultSpeciesCreationContextBuilder implements SpeciesCreationContextBuilder {
  private final SpeciesCreationPipeline pipeline;
  private final SpeciesCreationConfiguration configuration;
  private final SpeciesCreationEventManager eventManager;

  private File genomeFile;
  private Integer taxonomyId;
  private SequencesGroup fasta;
  private Map<Integer, List<String>> dictionary;

  DefaultSpeciesCreationContextBuilder(
    SpeciesCreationContext context
  ) {
    this(
      context.getPipeline(),
      context.getConfiguration(),
      context.getEventManager(),
      context.getGenomeFile().orElse(null),
      context.getTaxonomyId().orElse(null),
      context.getFasta().orElse(null),
      context.getDictionary().orElse(null)
    );
  }

  DefaultSpeciesCreationContextBuilder(
    SpeciesCreationPipeline pipeline,
    SpeciesCreationConfiguration configuration,
    SpeciesCreationEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null, null, null);
  }

  private DefaultSpeciesCreationContextBuilder(
    SpeciesCreationPipeline pipeline,
    SpeciesCreationConfiguration configuration,
    SpeciesCreationEventManager eventManager,
    File genomeFile,
    Integer taxonomyId,
    SequencesGroup fasta,
    Map<Integer, List<String>> dictionary
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.genomeFile = genomeFile;
    this.taxonomyId = taxonomyId;
    this.fasta = fasta;
    this.dictionary = dictionary;
  }

  @Override
  public SpeciesCreationContextBuilder addGenomeFile(File genomeFile) {
    this.genomeFile = genomeFile;
    return this;
  }

  @Override
  public SpeciesCreationContextBuilder addTaxonomyId(Integer taxonomyId) {
    this.taxonomyId = taxonomyId;
    return this;
  }
  
  @Override
  public SpeciesCreationContextBuilder addFasta(SequencesGroup fasta) {
    this.fasta = fasta;
    return this;
  }
  
  @Override
  public SpeciesCreationContextBuilder addDictionary(Map<Integer, List<String>> dictionary) {
    this.dictionary = dictionary;
    return this;
  }

  @Override
  public SpeciesCreationContext build() {
    return new DefaultSpeciesCreationContext(
      this.pipeline,
      this.configuration,
      this.eventManager,
      this.genomeFile,
      this.taxonomyId,
      this.fasta,
      this.dictionary
    );
  }
}
