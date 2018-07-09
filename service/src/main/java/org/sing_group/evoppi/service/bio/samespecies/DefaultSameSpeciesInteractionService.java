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

package org.sing_group.evoppi.service.bio.samespecies;

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesInteractionService;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineExecutor;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultSameSpeciesInteractionService implements SameSpeciesInteractionService {
  @Inject
  private PipelineExecutor executor;

  @Inject
  private SameSpeciesGeneInteractionsPipeline pipeline;
  
  @Override
  @Asynchronous
  public void calculateSameSpeciesInteractions(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) SameSpeciesInteractionsRequestEvent event
  ) {
    final SameSpeciesGeneInteractionsConfiguration configuration = new DefaultSameSpeciesGeneInteractionsConfiguration(
      event.getGeneId(), event.getInteractomes().toArray(), event.getMaxDegree(), event.getWorkId(), event.getWorkId()
    );
    
    this.executor.execute(pipeline, configuration);
  }
}
