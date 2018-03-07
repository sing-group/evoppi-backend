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
package org.sing_group.evoppi.service.bio.samespecies.pipeline;

import static java.util.Arrays.asList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.service.bio.samespecies.DefaultSameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;

@Stateless
@PermitAll
public class DefaultSameSpeciesGeneInteractionsPipeline
implements SameSpeciesGeneInteractionsPipeline {
  @Inject
  private SameSpeciesGeneInteractionsStep step;
  
  @Inject
  private SameSpeciesGeneInteractionsEventManager eventManager;
  
  @Override
  public String getName() {
    return "Gene interactions calculus";
  }

  @Override
  public List<SameSpeciesGeneInteractionsStep> getSteps() {
    return asList(this.step);
  }
  
  @Override
  public SameSpeciesGeneInteractionsContext createContext(SameSpeciesGeneInteractionsConfiguration configuration) {
    return new DefaultSameSpeciesGeneInteractionsContext(this, configuration, this.eventManager);
  }

  @PermitAll
  @Override
  public int getStepIndex(SameSpeciesGeneInteractionsStep step) {
    return SameSpeciesGeneInteractionsPipeline.super.getStepIndex(step);
  }

  @PermitAll
  @Override
  public List<SameSpeciesGeneInteractionsStep> getUnexecutedSteps(SameSpeciesGeneInteractionsContext context) {
    return SameSpeciesGeneInteractionsPipeline.super.getUnexecutedSteps(context);
  }

  @PermitAll
  @Override
  public List<SameSpeciesGeneInteractionsStep> getExecutedSteps(SameSpeciesGeneInteractionsContext context) {
    return SameSpeciesGeneInteractionsPipeline.super.getExecutedSteps(context);
  }

  @PermitAll
  @Override
  public int countTotalSteps() {
    return SameSpeciesGeneInteractionsPipeline.super.countTotalSteps();
  }
}
