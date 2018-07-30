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

package org.sing_group.evoppi.rest.resource;

import static org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies.createDependency;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

public final class Deployments {
  private Deployments() {}
  
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
      .addPackages(true, "org.sing_group.evoppi.rest")
      .addAsLibraries(
        Maven.resolver()
          .loadPomFromFile("pom.xml")
          .importCompileAndRuntimeDependencies()
          .addDependencies(createDependency("com.github.sleroy:fakesmtp-junit-runner", ScopeType.RUNTIME, false))
          .addDependencies(createDependency("org.sing_group:evoppi-domain", ScopeType.RUNTIME, false))
          .addDependencies(createDependency("org.sing_group:evoppi-service", ScopeType.RUNTIME, false))
          .addDependencies(createDependency("org.sing_group:evoppi-tests", ScopeType.TEST, false))
          .resolve().withTransitivity()
        .asFile()
      )
      .addAsResource("arquillian.extension.persistence.properties")
      .addAsResource("arquillian.extension.persistence.dbunit.properties")
      .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
      .addAsWebInfResource("jboss-web.xml")
      .addAsWebInfResource("web.xml")
      .addAsWebInfResource("beans.xml", "beans.xml");
  }
}
