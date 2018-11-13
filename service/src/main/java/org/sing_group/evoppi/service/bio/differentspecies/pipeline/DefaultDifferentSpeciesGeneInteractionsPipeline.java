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

import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.execution.pipeline.AbstractPipeline;

@Stateless
@PermitAll
@TransactionAttribute(NEVER)
public class DefaultDifferentSpeciesGeneInteractionsPipeline
extends AbstractPipeline<
  DifferentSpeciesGeneInteractionsConfiguration,
  DifferentSpeciesGeneInteractionsContext,
  DifferentSpeciesGeneInteractionsStep,
  DifferentSpeciesGeneInteractionsPipeline,
  DifferentSpeciesGeneInteractionsEvent,
  DifferentSpeciesGeneInteractionsEventManager
>
implements DifferentSpeciesGeneInteractionsPipeline {
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  DefaultDifferentSpeciesGeneInteractionsPipeline() {}
  
  public DefaultDifferentSpeciesGeneInteractionsPipeline(
    DifferentSpeciesGeneInteractionsEventManager eventManager,
    Collection<DifferentSpeciesGeneInteractionsStep> step
  ) {
    this.setEventManager(eventManager);
    this.setSteps(step);
  }
  
  @Inject
  @Override
  public void setEventManager(DifferentSpeciesGeneInteractionsEventManager eventManager) {
    super.setEventManager(eventManager);
  }

  @Inject
  public void setSteps(Instance<DifferentSpeciesGeneInteractionsStep> steps) {
    requireNonNull(steps);
    
    super.setSteps(StreamSupport.stream(steps.spliterator(), false)
      .filter(step -> step.getOrder() >= 0)
    .collect(toSet()));
  }

  @Inject
  public void setContextBuilderFactory(DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }
  
  @Override
  public String getName() {
    return "Different species gene interactions calculus";
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext createContext(
    DifferentSpeciesGeneInteractionsConfiguration configuration
  ) {
    return this.contextBuilderFactory.createBuilderFor(
      this, configuration, this.eventManager
    ).build();
  }
  
  // Methods explicitly override to force @PermitAll on them
  @Override
  public Stream<DifferentSpeciesGeneInteractionsStep> getSteps() {
    return super.getSteps();
  }
  
  @Override
  public int countTotalSteps() {
    return super.countTotalSteps();
  }
  
  @Override
  public Stream<DifferentSpeciesGeneInteractionsStep> getExecutedSteps(DifferentSpeciesGeneInteractionsContext context) {
    return super.getExecutedSteps(context);
  }
  
  @Override
  public Stream<DifferentSpeciesGeneInteractionsStep> getUnexecutedSteps(DifferentSpeciesGeneInteractionsContext context) {
    return super.getUnexecutedSteps(context);
  }

}
