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

package org.sing_group.evoppi.rest.resource.user;

import static javax.ws.rs.client.Entity.entity;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.sing_group.evoppi.domain.entities.UsersDataset.newResearcher;
import static org.sing_group.evoppi.domain.entities.UsersDataset.newResearcherEmail;
import static org.sing_group.evoppi.domain.entities.UsersDataset.newResearcherLogin;
import static org.sing_group.evoppi.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.evoppi.domain.entities.UsersDataset.registrationBadCode;
import static org.sing_group.evoppi.domain.entities.UsersDataset.registrationCode;
import static org.sing_group.evoppi.domain.entities.UsersDataset.researcher;
import static org.sing_group.evoppi.http.util.HasHttpHeader.hasHttpHeaderStartingWith;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasBadRequestStatus;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.evoppi.http.util.HasHttpStatus.hasUnauthorizedStatus;
import static org.sing_group.evoppi.rest.entity.notification.EmailDataset.emailForRegistration;
import static org.sing_group.evoppi.rest.entity.notification.IsEqualToEmailModel.containsEmailModelInOrder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;
import org.sing_group.evoppi.rest.resource.Deployments;

import com.github.sleroy.fakesmtp.core.ServerConfiguration;
import com.github.sleroy.junit.mail.server.test.FakeSmtpRule;

@RunWith(Arquillian.class)
public class RegistrationResourceIntegrationTest {
  private static final String BASE_PATH = "api/user/registration";

  @Rule
  public FakeSmtpRule smtpServer = new FakeSmtpRule(ServerConfiguration.create().port(2525).charset("UTF-8").relayDomains("email.com"));
  
  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Test
  @InSequence(1)
  @UsingDataSet("users.xml")
  public void beforeRegistration() {}
  
  @Test
  @InSequence(2)
  @RunAsClient
  public void testRegistration(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher newResearcher = newResearcher();
    final UserRegistrationData registration = new UserRegistrationData(
      newResearcher.getLogin(),
      newResearcher.getEmail(),
      passwordOf(newResearcher)
    );
    
    final Response response = webTarget.request()
      .post(entity(registration, MediaType.APPLICATION_JSON_TYPE));
    
    assertThat(response, hasCreatedStatus());
    
    final String resourceUri = webTarget.getUri().toString();
    
    assertThat(response, hasHttpHeaderStartingWith("Location", resourceUri));
    
    final String location = response.getHeaderString("Location");
    final String uuid = location.substring(location.lastIndexOf('/') + 1);

    assertThat(this.smtpServer.mailBox(), containsEmailModelInOrder(emailForRegistration(registration, uuid)));
  }

  @Test
  @InSequence(3)
  @ShouldMatchDataSet(
    value = {"users.xml", "registration.xml" },
    excludeColumns = { "registration.code", "registration.registrationDateTime" }
  )
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterRegistration() {}

  @Test
  @InSequence(11)
  @UsingDataSet("users.xml")
  public void beforeRegistrationExistingUserWithLogin() {}

  @Test
  @InSequence(12)
  @RunAsClient
  public void testRegistrationExistingUserWithLogin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher researcher = researcher();
    final UserRegistrationData registration = new UserRegistrationData(
      researcher.getLogin(),
      newResearcherEmail(),
      passwordOf(researcher)
    );
    
    final Response response = webTarget.request()
      .post(entity(registration, MediaType.APPLICATION_JSON_TYPE));
    
    assertThat(response, hasUnauthorizedStatus());
    
    assertThat(this.smtpServer.mailBox(), is(empty()));
  }

  @Test
  @InSequence(13)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterRegistrationExistingUserWithLogin() {}

  @Test
  @InSequence(21)
  @UsingDataSet("users.xml")
  public void beforeRegistrationExistingUserWithEmail() {}

  @Test
  @InSequence(22)
  @RunAsClient
  public void testRegistrationExistingUserWithEmail(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher researcher = researcher();
    final UserRegistrationData registration = new UserRegistrationData(
      newResearcherLogin(),
      researcher.getEmail(),
      passwordOf(researcher)
    );
    
    final Response response = webTarget.request()
      .post(entity(registration, MediaType.APPLICATION_JSON_TYPE));
    
    assertThat(response, hasUnauthorizedStatus());
    assertThat(this.smtpServer.mailBox(), is(empty()));
  }

  @Test
  @InSequence(23)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterRegistrationExistingUserWithEmail() {}

  @Test
  @InSequence(31)
  @UsingDataSet({ "users.xml", "registration.xml"})
  public void beforeRegistrationExistingRegistrationWithLogin() {}

  @Test
  @InSequence(32)
  @RunAsClient
  public void testRegistrationExistingRegistrationWithLogin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher newResearcher = newResearcher();
    final UserRegistrationData registration = new UserRegistrationData(
      newResearcher.getLogin(),
      "change" + newResearcher.getEmail(),
      passwordOf(newResearcher)
    );
    
    final Response response = webTarget.request()
      .post(entity(registration, MediaType.APPLICATION_JSON_TYPE));
    
    assertThat(response, hasUnauthorizedStatus());
    assertThat(this.smtpServer.mailBox(), is(empty()));
  }

  @Test
  @InSequence(33)
  @ShouldMatchDataSet(
    value = {"users.xml", "registration.xml" },
    excludeColumns = { "registration.code", "registration.registrationDateTime" }
  )
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterRegistrationExistingRegistrationWithLogin() {}

  @Test
  @InSequence(41)
  @UsingDataSet({ "users.xml", "registration.xml"})
  public void beforeRegistrationExistingRegistrationWithEmail() {}

  @Test
  @InSequence(42)
  @RunAsClient
  public void testRegistrationExistingRegistrationWithEmail(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Researcher newResearcher = newResearcher();
    final UserRegistrationData registration = new UserRegistrationData(
      "change" + newResearcher.getLogin(),
      newResearcher.getEmail(),
      passwordOf(newResearcher)
    );
    
    final Response response = webTarget.request()
      .post(entity(registration, MediaType.APPLICATION_JSON_TYPE));
    
    assertThat(response, hasUnauthorizedStatus());
    assertThat(this.smtpServer.mailBox(), is(empty()));
  }

  @Test
  @InSequence(43)
  @ShouldMatchDataSet(
    value = {"users.xml", "registration.xml" },
    excludeColumns = { "registration.code", "registration.registrationDateTime" }
  )
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterRegistrationExistingRegistrationWithEmail() {}
  
  @Test
  @InSequence(101)
  @UsingDataSet({"users.xml", "registration.xml" })
  public void beforeConfirmation() {}
  
  @Test
  @InSequence(102)
  @RunAsClient
  public void testConfirmation(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Response response = webTarget.path(registrationCode())
      .request()
    .post(Entity.text(""));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(103)
  @ShouldMatchDataSet({"users.xml", "confirmed-researcher.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterConfirmationWithBadCode() {}
  
  @Test
  @InSequence(111)
  @UsingDataSet({"users.xml", "registration.xml" })
  public void beforeConfirmationWithBadCode() {}
  
  @Test
  @InSequence(112)
  @RunAsClient
  public void testConfirmationWithBadCode(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Response response = webTarget.path(registrationBadCode())
      .request()
    .post(Entity.text(""));
    
    assertThat(response, hasBadRequestStatus());
  }

  @Test
  @InSequence(113)
  @ShouldMatchDataSet({"users.xml", "registration.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterConfirmation() {}
}
