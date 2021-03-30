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
package org.sing_group.evoppi.service.bio.species;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.event.SpeciesCreationEvent;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultSpeciesCreationPersistenceManager implements SpeciesCreationPersistenceManager {

  @Inject
  private SpeciesDAO speciesDao;

  @Override
  public void manageEvent(@Observes SpeciesCreationEvent event) {
    final Optional<String> step = event.getRunningStepId();

    if (step.isPresent() && event.getRunningStepStatus().get().equals(StepExecutionStatus.FINISHED)) {
      switch (step.get()) {
        case SpeciesCreationPipeline.SINGLE_PROCESS_SPECIES:
          manageSpeciesProcessEnd(event);
          break;
      }
    }
  }

  private void manageSpeciesProcessEnd(SpeciesCreationEvent event) {
    final SpeciesCreationContext context = event.getContext();

    if (!context.getFasta().isPresent()) {
      throw new RuntimeException("Step finished but genome FASTA file not available");
    }

    if (!context.getDictionary().isPresent()) {
      throw new RuntimeException("Step finished but dictionary file not available");
    }

    if (!context.getTaxonomyId().isPresent()) {
      throw new RuntimeException("Step finished but taxonomy ID not available");
    }

    this.speciesDao.createSpecies(
      context.getConfiguration().getData().getName(),
      context.getFasta().get(),
      context.getDictionary().get(),
      context.getTaxonomyId().get()
    );
  }
}
