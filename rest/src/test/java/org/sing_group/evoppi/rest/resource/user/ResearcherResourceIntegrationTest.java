/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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


package org.sing_group.evoppi.rest.resource.user;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.evoppi.domain.entities.UsersDataset.ADMIN_HTTP_BASIC_AUTH;
import static org.sing_group.evoppi.domain.entities.UsersDataset.countResearchers;
import static org.sing_group.evoppi.domain.entities.UsersDataset.modifiedResearcher;
import static org.sing_group.evoppi.domain.entities.UsersDataset.newPasswordOf;
import static org.sing_group.evoppi.domain.entities.UsersDataset.newResearcher;
import static org.sing_group.evoppi.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.evoppi.domain.entities.UsersDataset.researcher;
import static org.sing_group.evoppi.domain.entities.UsersDataset.researcherToDelete;
import static org.sing_group.evoppi.domain.entities.UsersDataset.researchers;
import static org.sing_group.evoppi.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.evoppi.http.util.HasHttpHeader.hasHttpHeaderContaining;
import static org.sing_group.evoppi.http.util.HasHttpHeader.hasHttpHeaderEndingWith;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.evoppi.rest.entity.GenericTypes.ResearcherDataListType.RESEARCHER_DATA_LIST_TYPE;
import static org.sing_group.evoppi.rest.entity.user.IsEqualToResearcher.containsResearchersInAnyOrder;
import static org.sing_group.evoppi.rest.entity.user.IsEqualToResearcher.containsResearchersInOrder;
import static org.sing_group.evoppi.rest.entity.user.IsEqualToResearcher.equalToResearcher;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.evoppi.rest.entity.mapper.user.DefaultUserMapper;
import org.sing_group.evoppi.rest.entity.user.ResearcherData;
import org.sing_group.evoppi.rest.entity.user.ResearcherEditionData;
import org.sing_group.evoppi.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class ResearcherResourceIntegrationTest {
  private static final String BASE_PATH = "api/researcher/";
  
  private UserMapper userMapper;

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Before
  public void setUp() {
    this.userMapper = new DefaultUserMapper();
  }

  @Test
  @InSequence(0)
  @UsingDataSet("users.xml")
  public void beforeGet() {}

  @Test
  @InSequence(1)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher researcher = researcher();
    
    final Response response = webTarget.path(researcher.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final ResearcherData userData = response.readEntity(ResearcherData.class);
    
    assertThat(userData, is(equalToResearcher(researcher)));
  }

  @Test
  @InSequence(2)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGet() {}

  @Test
  @InSequence(10)
  @UsingDataSet("users.xml")
  public void beforeList() {}

  @Test
  @InSequence(11)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testList(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Stream<Researcher> researchers = researchers();
    
    final Response response = webTarget
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeaderContaining("Access-Control-Allow-Headers", "X-Total-Count"));
    assertThat(response, hasHttpHeader("X-Total-Count", countResearchers()));
    
    final List<ResearcherData> userData = response.readEntity(RESEARCHER_DATA_LIST_TYPE);
    
    assertThat(userData, containsResearchersInAnyOrder(researchers));
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterList() {}

  @Test
  @InSequence(13)
  @UsingDataSet("users.xml")
  public void beforeListFiltered() {}

  @Test
  @InSequence(14)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListFiltered(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final int start = 1;
    final int end = 3;
    final Function<Researcher, String> getter = Researcher::getLogin;
    final String order = "login";
    final SortDirection sortDirection = SortDirection.ASC;
    
    final Stream<Researcher> researchers = researchers(
      start, end, getter, sortDirection
    );
    
    final Response response = webTarget
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("order", order)
      .queryParam("sort", sortDirection)
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeaderContaining("Access-Control-Allow-Headers", "X-Total-Count"));
    assertThat(response, hasHttpHeader("X-Total-Count", countResearchers()));
    
    final List<ResearcherData> researcherData = response.readEntity(RESEARCHER_DATA_LIST_TYPE);
    
    assertThat(researcherData, containsResearchersInOrder(researchers));
  }

  @Test
  @InSequence(15)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListFiltered() {}

  @Test
  @InSequence(20)
  @UsingDataSet("users.xml")
  public void beforeCreate() {}

  @Test
  @InSequence(21)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher newResearcher = newResearcher();
    final ResearcherEditionData userData = userMapper.toEditionData(newResearcher, passwordOf(newResearcher));
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", newResearcher.getLogin()));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-researcher.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreate() {}
  
  @Test
  @InSequence(30)
  @UsingDataSet("users.xml")
  public void beforeUpdate() {}

  @Test
  @InSequence(31)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testUpdate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher modifiedAdmin = modifiedResearcher();
    final ResearcherEditionData userData = userMapper.toEditionData(modifiedAdmin, newPasswordOf(modifiedAdmin));
    
    final Response response = webTarget
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-researcher.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterUpdate() {}

  @Test
  @InSequence(40)
  @UsingDataSet("users.xml")
  public void beforeDelete() {}

  @Test
  @InSequence(41)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDelete(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher researcher = researcherToDelete();
    
    final Response response = webTarget.path(researcher.getLogin())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-researcher.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}

}
