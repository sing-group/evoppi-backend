/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.resource.bio;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.sing_group.evoppi.domain.entities.execution.ExecutionStatus.COMPLETED;
import static org.sing_group.evoppi.rest.entity.bio.InteractionsDataset.sameSpeciesInteractionsWithMaxDegree;
import static org.sing_group.evoppi.rest.entity.bio.IsEqualToInteractionResultData.containsInteractionResultDataInAnyOrder;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class SameSpeciesInteractionResourceIntegrationTest {
  private static final String BASE_PATH = "api/interaction/";

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  @Test
  @InSequence(10)
  @UsingDataSet("interactions.xml")
  public void beforeDegree1() {}

  @Test(timeout = 5000)
  @InSequence(11)
  @RunAsClient
  public void testDegree1(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    testDegree(webTarget, 1);
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("interactions.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree1() {}

  @Test
  @InSequence(20)
  @UsingDataSet("interactions.xml")
  public void beforeDegree2() {}

  @Test(timeout = 5000)
  @InSequence(21)
  @RunAsClient
  public void testDegree2(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    testDegree(webTarget, 2);
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet("interactions.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree2() {}

  @Test
  @InSequence(30)
  @UsingDataSet("interactions.xml")
  public void beforeDegree3() {}

  @Test(timeout = 5000)
  @InSequence(31)
  @RunAsClient
  public void testDegree3(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    testDegree(webTarget, 3);
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet("interactions.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree3() {}
  

  private static void testDegree(
    ResteasyWebTarget webTarget, int maxDegree
  ) {
    final Response response = webTarget
      .queryParam("gene", 100)
      .queryParam("interactome", 1, 2)
      .queryParam("maxDegree", maxDegree)
      .request()
    .get();
    
    WorkData workData = response.readEntity(WorkData.class);
    final String uuid = workData.getId().getId();
    
    final SameSpeciesInteractionsResultData result = waitUntilCompletion(webTarget, uuid);
    
    final InteractionResultData[] interactions = result.getInteractions().getInteractions();
    
    assertThat(asList(interactions), containsInteractionResultDataInAnyOrder(sameSpeciesInteractionsWithMaxDegree(maxDegree)));
  }
  
  private static SameSpeciesInteractionsResultData waitUntilCompletion(ResteasyWebTarget webTarget, String uuid) {
    while (true) {
      final Response workResponse = webTarget.path("result").path(uuid)
        .request()
      .get();
      
      final SameSpeciesInteractionsResultData result =
        workResponse.readEntity(SameSpeciesInteractionsResultData.class);
      
      if (result.getStatus().equals(COMPLETED)) {
        return result;
      } else {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
