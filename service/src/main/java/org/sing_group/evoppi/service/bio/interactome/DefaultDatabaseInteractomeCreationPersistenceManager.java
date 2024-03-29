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
package org.sing_group.evoppi.service.bio.interactome;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.DatabaseInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeCollectionDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.InteractomeCollection;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.execution.StepExecutionStatus;
import org.sing_group.evoppi.service.bio.entity.DatabaseInteractomeCreationData;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEvent;
import org.sing_group.interactomesparser.evoppi.EvoPpiInteractomeProcessingStatistics;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultDatabaseInteractomeCreationPersistenceManager
  implements DatabaseInteractomeCreationPersistenceManager {

  @Inject
  private DatabaseInteractomeDAO databaseInteractomeDao;

  @Inject
  private SpeciesDAO speciesDao;
  
  @Inject
  private InteractomeCollectionDAO interactomeCollectionDao;

  @Override
  public void manageEvent(@Observes
  DatabaseInteractomeCreationEvent event) {
    final Optional<String> step = event.getRunningStepId();

    if (step.isPresent() && event.getRunningStepStatus().get().equals(StepExecutionStatus.FINISHED)) {
      switch (step.get()) {
        case DatabaseInteractomeCreationPipeline.SINGLE_PROCESS_INTERACTOME:
          manageInteractomeProcessEnd(event);
          break;
      }
    }
  }

  private void manageInteractomeProcessEnd(DatabaseInteractomeCreationEvent event) {
    final DatabaseInteractomeCreationContext context = event.getContext();
    final DatabaseInteractomeCreationData configuration = event.getContext().getConfiguration().getData();

    if (!context.getStatistics().isPresent()) {
      throw new RuntimeException("Step finished but interactome processing statistics not available");
    }

    if (!context.getInteractions().isPresent()) {
      throw new RuntimeException("Step finished but interactions are not available");
    }

    EvoPpiInteractomeProcessingStatistics stats = context.getStatistics().get();
    Species species = this.speciesDao.getSpecies(configuration.getSpeciesDbId());
    
    InteractomeCollection collection =
      this.interactomeCollectionDao.get(configuration.getInteractomeCollectionId());

    this.databaseInteractomeDao.create(
      configuration.getName(),
      configuration.getDbSource().getName(),
      stats.getOriginalInteractionsCount().orElse(null),
      stats.getOriginalUniqueInteractionsCount().orElse(null),
      stats.getOriginalUniqueGenesCount().orElse(null),
      stats.getNotToUniProtKbInteractionsCount().orElse(null),
      stats.getNotToUniProtKbGenesCount().orElse(null),
      stats.getNotToGeneIdInteractionsCount().orElse(null),
      stats.getNotToGeneIdGenesCount().orElse(null),
      stats.getFinalInteractionsCount().orElse(null),
      stats.getFinalGenesCount().orElse(null),
      stats.getRemovedInterSpeciesInteractionsCount().orElse(null),
      stats.getMultimappedToGeneIdGenesCount().orElse(null),
      species,
      context.getInteractions().get(),
      collection
    );
  }
}
