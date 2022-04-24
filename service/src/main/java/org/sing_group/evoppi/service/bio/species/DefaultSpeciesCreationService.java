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
package org.sing_group.evoppi.service.bio.species;

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

import org.sing_group.evoppi.service.bio.species.event.SpeciesCreationRequestEvent;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationService;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineExecutor;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultSpeciesCreationService implements SpeciesCreationService {
  @Inject
  private PipelineExecutor executor;

  @Inject
  private SpeciesCreationPipeline pipeline;

  @Override
  @Asynchronous
  public void createSpecies(
    @Observes(during = TransactionPhase.AFTER_SUCCESS)
    SpeciesCreationRequestEvent event
  ) {
    final SpeciesCreationConfiguration configuration =
      new DefaultSpeciesCreationConfiguration(event.getData(), event.getWorkId());

    this.executor.execute(pipeline, configuration);
  }
}
