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
package org.sing_group.evoppi.rest.entity.user;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.evoppi.domain.entities.IsEqualToEntity;
import org.sing_group.evoppi.domain.entities.user.Researcher;

public class IsEqualToResearcher extends IsEqualToEntity<Researcher, ResearcherData> {
  public IsEqualToResearcher(Researcher user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(ResearcherData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Researcher::getLogin, ResearcherData::getLogin, actual)
        && checkAttribute("email", Researcher::getEmail, ResearcherData::getEmail, actual);
    }
  }
  
  @Factory
  public static IsEqualToResearcher equalToResearcher(Researcher researcher) {
    return new IsEqualToResearcher(researcher);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInAnyOrder(Researcher... researchers) {
    return containsEntityInAnyOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInAnyOrder(Iterable<Researcher> researchers) {
    return containsEntityInAnyOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInAnyOrder(Stream<Researcher> researchers) {
    return containsEntityInAnyOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInOrder(Researcher... researchers) {
    return containsEntityInOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInOrder(Iterable<Researcher> researchers) {
    return containsEntityInOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ResearcherData>> containsResearchersInOrder(Stream<Researcher> researchers) {
    return containsEntityInOrder(IsEqualToResearcher::equalToResearcher, researchers);
  }
}
