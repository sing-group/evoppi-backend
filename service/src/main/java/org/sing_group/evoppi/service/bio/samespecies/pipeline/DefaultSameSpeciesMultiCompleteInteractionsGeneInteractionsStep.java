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

import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.MultipleSameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;

@Stateless
@PermitAll
public class DefaultSameSpeciesMultiCompleteInteractionsGeneInteractionsStep
implements MultipleSameSpeciesGeneInteractionsStep {
  private Instance<DefaultSameSpeciesCompleteInteractionsGeneInteractionsStep> stepInstances;
  
  DefaultSameSpeciesMultiCompleteInteractionsGeneInteractionsStep() {}
  
  public DefaultSameSpeciesMultiCompleteInteractionsGeneInteractionsStep(
    Instance<DefaultSameSpeciesCompleteInteractionsGeneInteractionsStep> stepInstances
  ) {
    this.setStepInstances(stepInstances);
  }

  @Inject
  public void setStepInstances(Instance<DefaultSameSpeciesCompleteInteractionsGeneInteractionsStep> stepInstances) {
    this.stepInstances = stepInstances;
  }

  @Override
  public String getName() {
    return "Multi interactome gene interactions completion";
  }
  
  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public boolean isComplete(SameSpeciesGeneInteractionsContext context) {
    return context.getCompletedInteractions().isPresent();
  }

  @Transactional(SUPPORTS)
  @Override
  public Stream<SameSpeciesGeneInteractionsStep> getSteps(SameSpeciesGeneInteractionsContext context) {
    final SameSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();
    
    if (configuration.getMaxDegree() == 1 || configuration.getInteractomes().count() == 1) {
      return Stream.empty();
    } else {
      return configuration.getInteractomes()
        .mapToObj(interactomeId ->  {
          final DefaultSameSpeciesCompleteInteractionsGeneInteractionsStep step = stepInstances.get();
          step.setInteractomeId(interactomeId);
          return step;
        });
    }
  }

  @Override
  public OptionalInt countSteps(SameSpeciesGeneInteractionsContext context) {
    final SameSpeciesGeneInteractionsConfiguration configuration = context.getConfiguration();

    if (configuration.getMaxDegree() == 1 || configuration.getInteractomes().count() == 1) {
      return OptionalInt.empty();
    } else {
      return OptionalInt.of((int) context.getConfiguration().getInteractomes().count());
    }
  }
}
