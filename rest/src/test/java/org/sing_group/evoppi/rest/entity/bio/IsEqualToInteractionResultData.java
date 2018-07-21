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


package org.sing_group.evoppi.rest.entity.bio;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.evoppi.domain.entities.IsEqualToEntityOfSameType;

public class IsEqualToInteractionResultData extends IsEqualToEntityOfSameType<InteractionResultData> {
  public IsEqualToInteractionResultData(InteractionResultData result) {
    super(result);
  }

  @Override
  protected boolean matchesSafely(InteractionResultData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("geneA", InteractionResultData::getGeneA, actual)
        && checkAttribute("geneB", InteractionResultData::getGeneB, actual)
        && checkAttribute("interactomeDegrees", InteractionResultData::getInteractomeDegrees, actual);
    }
  }
  
  @Factory
  public static IsEqualToInteractionResultData equalToInteractionResultData(InteractionResultData result) {
    return new IsEqualToInteractionResultData(result);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInAnyOrder(InteractionResultData... results) {
    return containsEntityInAnyOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInAnyOrder(Iterable<InteractionResultData> results) {
    return containsEntityInAnyOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInAnyOrder(Stream<InteractionResultData> results) {
    return containsEntityInAnyOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInOrder(InteractionResultData... results) {
    return containsEntityInOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInOrder(Iterable<InteractionResultData> results) {
    return containsEntityInOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InteractionResultData>> containsInteractionResultDataInOrder(Stream<InteractionResultData> results) {
    return containsEntityInOrder(IsEqualToInteractionResultData::equalToInteractionResultData, results);
  }
}
