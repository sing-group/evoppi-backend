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

import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.event.SameSpeciesInteractionsCalculusStartedEvent;
import org.sing_group.evoppi.service.entity.bio.GeneInteraction;
import org.sing_group.evoppi.service.spi.bio.event.SameSpeciesInteractionEventNotifier;


@Stateless
@PermitAll
public class DefaultSameSpeciesInteractionEventNotifier implements SameSpeciesInteractionEventNotifier {
  
  @Inject
  private Event<SameSpeciesCalculusStartedEvent> startEvents;
  
  @Inject
  private Event<SameSpeciesInteractionsCalculusStartedEvent> startDegreeEvents;
  
  @Inject
  private Event<SameSpeciesInteractionCalculusFinishedEvent> finishDegreeEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFinishedEvent> finishEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFailedEvent> failureEvents;

  @Override
  public void notifyCalculusStarted(SameSpeciesCalculusEvent baseEvent) {
    startEvents.fire(new SameSpeciesCalculusStartedEvent(baseEvent));
  }

  @Override
  public void notifyDegreeCalculusStarted(SameSpeciesCalculusEvent baseEvent, int degree) {
    startDegreeEvents.fire(new SameSpeciesInteractionsCalculusStartedEvent(baseEvent, degree));
  }
  
  @Override
  public void notifyDegreeCalculusFinished(SameSpeciesCalculusEvent baseEvent, int degree, Collection<GeneInteraction> interactions) {
    finishDegreeEvents.fire(new SameSpeciesInteractionCalculusFinishedEvent(baseEvent, degree, interactions));
  }
  
  @Override
  public void notifyCalculusFinished(SameSpeciesCalculusEvent baseEvent) {
    finishEvents.fire(new SameSpeciesCalculusFinishedEvent(baseEvent));
  }

  @Override
  public void notifyCalculusFailed(SameSpeciesCalculusEvent baseEvent, String cause) {
    failureEvents.fire(new SameSpeciesCalculusFailedEvent(baseEvent, cause));
  }

}
