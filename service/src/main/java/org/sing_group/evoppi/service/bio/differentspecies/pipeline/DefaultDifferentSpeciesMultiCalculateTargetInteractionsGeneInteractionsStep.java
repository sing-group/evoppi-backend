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

package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static javax.transaction.Transactional.TxType.SUPPORTS;
import static org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline.MULTIPLE_CACULATE_TARGET_INTERACTIONS_STEP_ID;

import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.MultipleDifferentSpeciesGeneInteractionsStep;

@Stateless
@PermitAll
public class DefaultDifferentSpeciesMultiCalculateTargetInteractionsGeneInteractionsStep
implements MultipleDifferentSpeciesGeneInteractionsStep {
  private Instance<DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep> stepInstances;
  
  DefaultDifferentSpeciesMultiCalculateTargetInteractionsGeneInteractionsStep() {}
  
  public DefaultDifferentSpeciesMultiCalculateTargetInteractionsGeneInteractionsStep(
    Instance<DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep> stepInstances
  ) {
    this.setStepInstances(stepInstances);
  }

  @Inject
  public void setStepInstances(Instance<DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep> stepInstances) {
    this.stepInstances = stepInstances;
  }
  
  @Override
  public String getStepId() {
    return MULTIPLE_CACULATE_TARGET_INTERACTIONS_STEP_ID;
  }

  @Override
  public String getName() {
    return "Multi target interactome target gene interactions calculation";
  }
  
  @Override
  public int getOrder() {
    return 5;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getTargetInteractions().isPresent();
  }

  @Transactional(SUPPORTS)
  @Override
  public Stream<DifferentSpeciesGeneInteractionsStep> getSteps(DifferentSpeciesGeneInteractionsContext context) {
    return context.getConfiguration().getTargetInteractomes()
      .mapToObj(interactomeId ->  {
        final DefaultDifferentSpeciesCalculateTargetInteractionsGeneInteractionsStep step = stepInstances.get();
        step.setInteractomeId(interactomeId);
        return step;
      });
  }

  @Override
  public OptionalInt countSteps(DifferentSpeciesGeneInteractionsContext context) {
    return OptionalInt.of((int) context.getConfiguration().getTargetInteractomes().count());
  }
}
