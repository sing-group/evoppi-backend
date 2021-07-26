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
package org.sing_group.evoppi.service.bio.interactome.pipeline;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static javax.ejb.TransactionAttributeType.NEVER;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationStep;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEvent;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.AbstractPipeline;

@Stateless
@PermitAll
@TransactionAttribute(NEVER)
public class DefaultDatabaseInteractomeCreationPipeline
extends AbstractPipeline<
  DatabaseInteractomeCreationConfiguration,
  DatabaseInteractomeCreationContext,
  DatabaseInteractomeCreationStep,
  DatabaseInteractomeCreationPipeline,
  DatabaseInteractomeCreationEvent,
  DatabaseInteractomeCreationEventManager
>
  implements DatabaseInteractomeCreationPipeline {
  private DatabaseInteractomeCreationContextBuilderFactory contextBuilderFactory;

  DefaultDatabaseInteractomeCreationPipeline() {}

  public DefaultDatabaseInteractomeCreationPipeline(
    DatabaseInteractomeCreationEventManager eventManager,
    Collection<DatabaseInteractomeCreationStep> step
  ) {
    this.setEventManager(eventManager);
    this.setSteps(step);
  }

  @Inject
  @Override
  public void setEventManager(DatabaseInteractomeCreationEventManager eventManager) {
    super.setEventManager(eventManager);
  }

  @Inject
  public void setSteps(Instance<DatabaseInteractomeCreationStep> steps) {
    requireNonNull(steps);

    super.setSteps(
      StreamSupport.stream(steps.spliterator(), false)
        .filter(step -> step.getOrder() >= 0)
        .collect(toSet())
    );
  }

  @Inject
  public void setContextBuilderFactory(DatabaseInteractomeCreationContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }

  @Override
  public String getName() {
    return "Interactome processing pipeline";
  }

  @Override
  public DatabaseInteractomeCreationContext createContext(DatabaseInteractomeCreationConfiguration configuration) {
    return this.contextBuilderFactory.createBuilderFor(
      this, configuration, this.eventManager
    ).build();
  }

  // Methods explicitly override to force @PermitAll on them
  @Override
  public Stream<DatabaseInteractomeCreationStep> getSteps() {
    return super.getSteps();
  }

  @Override
  public int countTotalSteps() {
    return super.countTotalSteps();
  }

  @Override
  public Stream<DatabaseInteractomeCreationStep> getExecutedSteps(DatabaseInteractomeCreationContext context) {
    return super.getExecutedSteps(context);
  }

  @Override
  public Stream<DatabaseInteractomeCreationStep> getUnexecutedSteps(DatabaseInteractomeCreationContext context) {
    return super.getUnexecutedSteps(context);
  }
}
