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
package org.sing_group.evoppi.service.bio;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsRequestEvent;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.InteractionsCalculator;
import org.sing_group.evoppi.service.spi.bio.SameSpeciesInteractionService;
import org.sing_group.evoppi.service.spi.bio.event.InteractionsCalculusCallback;

@Stateless
@PermitAll
public class DefaultSameSpeciesInteractionService implements SameSpeciesInteractionService {
  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private InteractomeDAO interactomeDao;
  
  @Inject
  private Event<SameSpeciesCalculusStartedEvent> startEvents;
  
  @Inject
  private Event<SameSpeciesInteractionsCalculusStartedEvent> startDegreeEvents;
  
  @Inject
  private Event<SameSpeciesInteractionCalculusFinishedEvent> finishDegreeEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFinishedEvent> finishEvents;
  
  @Inject
  private InteractionsCalculator interactionsCalculator;
  
  @Override
  @Asynchronous
  public void calculateSameSpeciesInteractions(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) SameSpeciesInteractionsRequestEvent event
  ) {
    final InteractionsCalculusCallback callback = new BridgeInteractionsCalculusCallback(event);
    
    final Gene gene = this.geneDao.getGene(event.getGeneId());
    final Collection<Interactome> interactomes = event.getInteractomes()
      .mapToObj(this.interactomeDao::getInteractome)
    .collect(toSet());
    
    this.interactionsCalculator.calculateInteractions(gene, interactomes, event.getMaxDegree(), callback);
  }
  
  private class BridgeInteractionsCalculusCallback implements InteractionsCalculusCallback {
    private final SameSpeciesInteractionsRequestEvent baseEvent;
    
    private BridgeInteractionsCalculusCallback(SameSpeciesInteractionsRequestEvent baseEvent) {
      this.baseEvent = baseEvent;
    }
    
    @Override
    public void calculusStarted() {
      startEvents.fire(new SameSpeciesCalculusStartedEvent(this.baseEvent));
    }

    @Override
    public void degreeCalculusStarted(int degree) {
      startDegreeEvents.fire(new SameSpeciesInteractionsCalculusStartedEvent(this.baseEvent, degree));
    }
    
    @Override
    public void degreeCalculusFinished(int degree, Stream<GeneInteraction> interactions) {
      finishDegreeEvents.fire(new SameSpeciesInteractionCalculusFinishedEvent(this.baseEvent, degree, interactions.collect(toSet())));
    }
    
    @Override
    public void calculusFinished() {
      finishEvents.fire(new SameSpeciesCalculusFinishedEvent(this.baseEvent));
    }
  }
}
