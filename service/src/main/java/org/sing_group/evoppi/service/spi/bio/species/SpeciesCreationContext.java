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

package org.sing_group.evoppi.service.spi.bio.species;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationStep;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEvent;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineContext;
import org.sing_group.seda.datatype.SequencesGroup;

public interface SpeciesCreationContext
extends PipelineContext<
  SpeciesCreationConfiguration,
  SpeciesCreationContext,
  SpeciesCreationStep,
  SpeciesCreationPipeline,
  SpeciesCreationEvent,
  SpeciesCreationEventManager
> {
  Optional<File> getGenomeFile();

  Optional<Integer> getTaxonomyId();

  Optional<SequencesGroup> getFasta();

  Optional<Map<Integer, List<String>>> getDictionary();
}
