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

import static javax.transaction.Transactional.TxType.SUPPORTS;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.MULTIPLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID;

import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.MultipleDifferentSpeciesGeneInteractionsStep;

@Stateless
@PermitAll
public class DefaultDifferentSpeciesMultiCompleteTargetInteractionsGeneInteractionsStep
implements MultipleDifferentSpeciesGeneInteractionsStep {
  private Instance<DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep> stepInstances;
  
  DefaultDifferentSpeciesMultiCompleteTargetInteractionsGeneInteractionsStep() {}
  
  public DefaultDifferentSpeciesMultiCompleteTargetInteractionsGeneInteractionsStep(
    Instance<DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep> stepInstances
  ) {
    this.setStepInstances(stepInstances);
  }

  @Inject
  public void setStepInstances(Instance<DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep> stepInstances) {
    this.stepInstances = stepInstances;
  }
  
  @Override
  public String getStepId() {
    return MULTIPLE_COMPLETE_TARGET_INTERACTIONS_STEP_ID;
  }

  @Override
  public String getName() {
    return "Multi target interactome target gene interactions completion";
  }
  
  @Override
  public int getOrder() {
    return 6;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getTargetCompletedInteractions().isPresent();
  }

  @Transactional(SUPPORTS)
  @Override
  public Stream<DifferentSpeciesGeneInteractionsStep> getSteps(DifferentSpeciesGeneInteractionsContext context) {
    final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    
    if (configuration.getMaxDegree() == 1) {
      return Stream.empty();
    } else {
      return configuration.getTargetInteractomes()
        .mapToObj(interactomeId ->  {
          final DefaultDifferentSpeciesCompleteTargetInteractionsGeneInteractionsStep step = stepInstances.get();
          step.setInteractomeId(interactomeId);
          return step;
        });
    }
  }

  @Override
  public OptionalInt countSteps(DifferentSpeciesGeneInteractionsContext context) {
    final DifferentSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();

    if (configuration.getMaxDegree() == 1) {
      return OptionalInt.empty();
    } else {
      return OptionalInt.of((int) context.getConfiguration().getTargetInteractomes().count());
    }
  }
}
