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

import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.evoppi.service.bio.entity.GeneInteraction;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesCalculusEvent;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesCalculusFailedEvent;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesCalculusFinishedEvent;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesCalculusStartedEvent;
import org.sing_group.evoppi.service.bio.samespecies.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.event.SameSpeciesInteractionEventNotifier;


@Stateless
@PermitAll
public class DefaultSameSpeciesInteractionEventNotifier implements SameSpeciesInteractionEventNotifier {
  
  @Inject
  private Event<SameSpeciesCalculusStartedEvent> startEvents;
  
  @Inject
  private Event<SameSpeciesGeneInteractionsEvent> interactionsEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFinishedEvent> finishEvents;
  
  @Inject
  private Event<SameSpeciesCalculusFailedEvent> failureEvents;

  @Override
  public void notifyCalculusStarted(SameSpeciesCalculusEvent baseEvent) {
    startEvents.fire(new SameSpeciesCalculusStartedEvent(baseEvent));
  }
  
  @Override
  public void notifyInteractionsCalculusFinished(
    SameSpeciesCalculusEvent event, Collection<GeneInteraction> interactions
  ) {
    this.interactionsEvents.fire(new SameSpeciesGeneInteractionsEvent(event, interactions));
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
