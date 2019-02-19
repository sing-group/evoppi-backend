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

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.assertThat;
import static org.sing_group.evoppi.rest.entity.bio.IsEqualToInteractionResultData.containsInteractionResultDataInAnyOrder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsDataset;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.resource.Deployments;

public abstract class InteractionResourceIntegrationTest {

  private static final String BASE_PATH = "api/interaction/";
  private static final Path FILES_PATH = Paths.get("/tmp/evoppi");

  private final InteractionsDataset expectedDataset;
  
  public InteractionResourceIntegrationTest(InteractionsDataset expectedDataset) {
    super();
    
    this.expectedDataset = expectedDataset;
  }
  
  protected abstract InteractionResultData[] mapResponseToInteractions(Response response);
  
  protected abstract ResteasyWebTarget prepareQuery(ResteasyWebTarget webTarget);
  
  @BeforeClass
  @AfterClass
  public static void deleteFiles() throws IOException {
    removeRecursively(FILES_PATH);
  }
  
  private static void removeRecursively(Path path) throws IOException {
    if (Files.isDirectory(path)) {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.delete(file);
          return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          Files.delete(dir);
          return FileVisitResult.CONTINUE;
        }
      });
    } else if (Files.isRegularFile(path)) {
      Files.delete(path);
    }
  }
  
  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  private void testDegree(ResteasyWebTarget webTarget, int maxDegree) {
    final Response response = prepareQuery(webTarget)
      .queryParam("maxDegree", maxDegree)
      .queryParam("minimumIdentity", 0.9)
      .request()
    .get();
    
    WorkData workData = response.readEntity(WorkData.class);
    final String uuid = workData.getId().getId();
    
    final InteractionResultData[] interactions = waitUntilCompletion(webTarget, uuid);
    
    System.out.println("DEGREE " + maxDegree);
    final InteractionResultData[] expectedInteractions = this.expectedDataset.interactionsWithMaxDegree(maxDegree).toArray(InteractionResultData[]::new);
    
    printComparison(expectedInteractions, interactions);
    
    assertThat(asList(interactions), containsInteractionResultDataInAnyOrder(expectedInteractions));
  }

  public abstract void beforeDegree1();

  @Test(timeout = 30000)
  @InSequence(11)
  @RunAsClient
  public void testDegree1(@ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget) {
    testDegree(webTarget, 1);
  }

  public abstract void afterDegree1();

  public abstract void beforeDegree2();

  @Test(timeout = 30000)
  @InSequence(21)
  @RunAsClient
  public void testDegree2(@ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget) {
    testDegree(webTarget, 2);
  }

  public abstract void afterDegree2();

  public abstract void beforeDegree3();

  @Test(timeout = 30000)
  @InSequence(31)
  @RunAsClient
  public void testDegree3(@ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget) {
    testDegree(webTarget, 3);
  }

  public abstract void afterDegree3();

  private static void printComparison(InteractionResultData[] expected, InteractionResultData[] actual) {
    final Predicate<InteractionResultData> isExpected = interaction -> stream(expected).anyMatch(in -> in == interaction);
    final Predicate<InteractionResultData> isActual = interaction -> stream(actual).anyMatch(in -> in == interaction);
  
    final Map<String, List<InteractionResultData>> interactions = new TreeMap<>(Stream.concat(stream(expected), stream(actual))
      .collect(groupingBy(interaction -> interaction.getGeneA() + " # " + interaction.getGeneB())));
    
    for (Map.Entry<String, List<InteractionResultData>> entry : interactions.entrySet()) {
      System.out.print(entry.getKey() + " - E: ");
      
      for (InteractionResultData interaction : entry.getValue()) {
        if (isExpected.test(interaction)) {
          System.out.print(interaction.getInteractomeDegrees());
        }
      }
      
      System.out.print(" - A: ");
      
      for (InteractionResultData interaction : entry.getValue()) {
        if (isActual.test(interaction)) {
          System.out.print(interaction.getInteractomeDegrees());
        }
      }
      
      System.out.println();
    }
    
  }

  private InteractionResultData[] waitUntilCompletion(ResteasyWebTarget webTarget, String uuid) {
    while (true) {
      final Response workResponse = webTarget.path("result").path(uuid)
        .request()
      .get();
      
      final InteractionResultData[] result = this.mapResponseToInteractions(workResponse);
      
      if (result != null) {
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
