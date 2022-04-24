/*-
 * #%L
 * REST
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
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeDataset;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;

@RunWith(Arquillian.class)
public class DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeIntegrationTest
extends InteractionResourceIntegrationTest {

  public DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeIntegrationTest() {
    super(new DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeDataset());
  }

  @Override
  protected InteractionResultData[] mapResponseToInteractions(Response response) {
    final DifferentSpeciesInteractionsResultData result = response.readEntity(DifferentSpeciesInteractionsResultData.class);
    
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
      .queryParam("referenceInteractome", 1, 2)
      .queryParam("targetInteractome", 3, 4);
  }

  @Test
  @InSequence(10)
  @UsingDataSet("different-species-interactions-missing-query-gene.xml")
  public void beforeDegree1() {}

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("different-species-interactions-missing-query-gene.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree1() {}

  @Test
  @InSequence(20)
  @UsingDataSet("different-species-interactions-missing-query-gene.xml")
  public void beforeDegree2() {}

  @Test
  @InSequence(22)
  @ShouldMatchDataSet("different-species-interactions-missing-query-gene.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree2() {}

  @Test
  @InSequence(30)
  @UsingDataSet("different-species-interactions-missing-query-gene.xml")
  public void beforeDegree3() {}

  @Test
  @InSequence(32)
  @ShouldMatchDataSet("different-species-interactions-missing-query-gene.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDegree3() {}
  
}
