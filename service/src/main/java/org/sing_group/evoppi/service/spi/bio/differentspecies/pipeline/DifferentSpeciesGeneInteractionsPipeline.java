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

package org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline;

import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.Pipeline;

public interface DifferentSpeciesGeneInteractionsPipeline
extends Pipeline<
  DifferentSpeciesGeneInteractionsConfiguration,
  DifferentSpeciesGeneInteractionsContext,
  DifferentSpeciesGeneInteractionsStep,
  DifferentSpeciesGeneInteractionsPipeline,
  DifferentSpeciesGeneInteractionsEvent,
  DifferentSpeciesGeneInteractionsEventManager
> {
  public static final String MULTIPLE_CACULATE_REFERENCE_INTERACTIONS_STEP_ID = "MULTIPLE CALCULATE REFERENCE INTERACTIONS";
  public static final String SINGLE_CACULATE_REFERENCE_INTERACTIONS_STEP_ID = "SINGLE CALCULATE REFERENCE INTERACTIONS";
  public static final String MULTIPLE_CACULATE_TARGET_INTERACTIONS_STEP_ID = "MULTIPLE CALCULATE TARGET INTERACTIONS";
  public static final String SINGLE_CACULATE_TARGET_INTERACTIONS_STEP_ID = "SINGLE CALCULATE TARGET INTERACTIONS";
  public static final String GENERATE_REFERENCE_FASTA_STEP_ID = "GENERATE REFERENCE FASTA";
  public static final String GENERATE_TARGET_FASTA_STEP_ID = "GENERATE TARGET FASTA";
  public static final String BLAST_ALIGNMENT_STEP_ID = "BLAST ALIGNMENT";
  public static final String MULTIPLE_COMPLETE_REFERENCE_INTERACTIONS_STEP_ID = "MULTIPLE COMPLETE REFERENCE INTERACTIONS";
  public static final String SINGLE_COMPLETE_REFERENCE_INTERACTIONS_STEP_ID = "SINGLE COMPLETE REFERENCE INTERACTIONS";
  public static final String MULTIPLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID = "MULTIPLE COMPLETE TARGET INTERACTIONS";
  public static final String SINGLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID = "SINGLE COMPLETE TARGET INTERACTIONS";
}
