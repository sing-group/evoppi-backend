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

import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.MultipleSameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;

@Stateless
@PermitAll
public class DefaultSameSpeciesMultiCalculateInteractionsGeneInteractionsStep
implements MultipleSameSpeciesGeneInteractionsStep {
  private Instance<DefaultSameSpeciesCalculateInteractionsGeneInteractionsStep> stepInstances;
  
  DefaultSameSpeciesMultiCalculateInteractionsGeneInteractionsStep() {}
  
  public DefaultSameSpeciesMultiCalculateInteractionsGeneInteractionsStep(
    Instance<DefaultSameSpeciesCalculateInteractionsGeneInteractionsStep> stepInstances
  ) {
    this.setStepInstances(stepInstances);
  }

  @Inject
  public void setStepInstances(Instance<DefaultSameSpeciesCalculateInteractionsGeneInteractionsStep> stepInstances) {
    this.stepInstances = stepInstances;
  }

  @Override
  public String getName() {
    return "Multi interactome gene interactions calculation";
  }
  
  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public boolean isComplete(SameSpeciesGeneInteractionsContext context) {
    return context.getInteractions().isPresent();
  }

  @Transactional(SUPPORTS)
  @Override
  public Stream<SameSpeciesGeneInteractionsStep> getSteps(SameSpeciesGeneInteractionsContext context) {
    return context.getConfiguration().getInteractomes()
      .mapToObj(interactomeId ->  {
        final DefaultSameSpeciesCalculateInteractionsGeneInteractionsStep step = stepInstances.get();
        step.setInteractomeId(interactomeId);
        return step;
      });
  }

  @Override
  public OptionalInt countSteps(SameSpeciesGeneInteractionsContext context) {
    return OptionalInt.of((int) context.getConfiguration().getInteractomes().count());
  }
}
