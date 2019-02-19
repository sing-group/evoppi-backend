/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

package org.sing_group.evoppi.rest.resource.bio;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.evoppi.domain.entities.execution.ExecutionStatus;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsDataset;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;

@RunWith(Arquillian.class)
public class SameSpeciesInteractionResourceIntegrationTest
extends InteractionResourceIntegrationTest {

  public SameSpeciesInteractionResourceIntegrationTest() {
    super(new SameSpeciesInteractionsDataset());
  }
  
  @Override
  protected InteractionResultData[] mapResponseToInteractions(Response response) {
    final SameSpeciesInteractionsResultData result = response.readEntity(SameSpeciesInteractionsResultData.class);
    
    if (result.getStatus().equals(ExecutionStatus.COMPLETED)) {
      return result.getInteractions().getInteractions();
    } else {
      return null;
    }
  }
  
  @Override
  protected ResteasyWebTarget prepareQuery(ResteasyWebTarget webTarget) {
    return webTarget
      .queryParam("gene", 100)
      .queryParam("interactome", 1, 2);
  }
  
  @Test
  @InSequence(10)
  @UsingDataSet("same-species-interactions.xml")
  public void beforeDegree1() {}

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("same-species-interactions.xml")
  @CleanupUsingScript({
    "cleanup.sql", "cleanup-autoincrement.sql"
  })
  public void afterDegree1() {}

  @Test
  @InSequence(20)
  @UsingDataSet("same-species-interactions.xml")
  public void beforeDegree2() {}

  @Test
  @InSequence(22)
  @ShouldMatchDataSet("same-species-interactions.xml")
  @CleanupUsingScript({
    "cleanup.sql", "cleanup-autoincrement.sql"
  })
  public void afterDegree2() {}

  @Test
  @InSequence(30)
  @UsingDataSet("same-species-interactions.xml")
  public void beforeDegree3() {}

  @Test
  @InSequence(32)
  @ShouldMatchDataSet("same-species-interactions.xml")
  @CleanupUsingScript({
    "cleanup.sql", "cleanup-autoincrement.sql"
  })
  public void afterDegree3() {}
}
