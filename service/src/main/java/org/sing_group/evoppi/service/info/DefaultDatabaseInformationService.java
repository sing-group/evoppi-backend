/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.info;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.info.DatabaseInformationDao;
import org.sing_group.evoppi.domain.entities.DatabaseInformation;
import org.sing_group.evoppi.service.spi.bio.DatabaseInteractomeService;
import org.sing_group.evoppi.service.spi.bio.GeneService;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.PredictomeService;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;
import org.sing_group.evoppi.service.spi.info.DatabaseInformationService;

@Startup
@Singleton
@PermitAll
public class DefaultDatabaseInformationService implements DatabaseInformationService {

  @Inject
  private DatabaseInformationDao databaseInformationDao;

  @Inject
  private SpeciesService speciesService;

  @Inject
  private DatabaseInteractomeService dbInteractomeService;

  @Inject
  private PredictomeService predictomeService;

  @Inject
  private GeneService geneService;

  @Inject
  private InteractionService interactionService;

  private long speciesCount;
  private long databaseInteractomesCount;
  private long predictomesCount;
  private long genesCount;
  private long interactionsCount;

  @PostConstruct
  private void postConstruct() {
    this.updateStatistics();
  }

  private void updateStatistics() {
    System.out.println("Initializing database statistics");

    this.speciesCount = this.speciesService.count();
    this.databaseInteractomesCount = this.dbInteractomeService.count();
    this.predictomesCount = this.predictomeService.count();
    this.genesCount = this.geneService.count();
    this.interactionsCount = this.interactionService.count();

    System.out.println("Database statistics ready:");
    System.out.println("Species count: " + this.getSpeciesCount());
    System.out.println("Database interactomes count: " + this.getDatabaseInteractomesCount());
    System.out.println("Predictomes count: " + this.getPredictomesCount());
    System.out.println("Genes count: " + this.getGenesCount());
    System.out.println("Interactions count: " + this.getInteractionsCount());
  }

  @Override
  public Optional<DatabaseInformation> getDbVersion() {
    return this.databaseInformationDao.get();
  }

  @Override
  public long getSpeciesCount() {
    return speciesCount;
  }

  @Override
  public long getDatabaseInteractomesCount() {
    return databaseInteractomesCount;
  }

  @Override
  public long getPredictomesCount() {
    return predictomesCount;
  }

  @Override
  public long getGenesCount() {
    return genesCount;
  }

  @Override
  public long getInteractionsCount() {
    return interactionsCount;
  }
}
