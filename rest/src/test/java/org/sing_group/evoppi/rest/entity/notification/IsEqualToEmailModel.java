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

package org.sing_group.evoppi.rest.entity.notification;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.evoppi.domain.entities.IsEqualToEntityOfSameType;

import com.github.sleroy.fakesmtp.model.EmailModel;

public class IsEqualToEmailModel extends IsEqualToEntityOfSameType<EmailModel> {
  public IsEqualToEmailModel(EmailModel email) {
    super(email);
  }

  @Override
  protected boolean matchesSafely(EmailModel actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("getFrom", EmailModel::getFrom, actual)
        && checkAttribute("getTo", EmailModel::getTo, actual)
        && checkAttribute("subject", EmailModel::getSubject, actual)
        && checkAttribute("emailStr", EmailModel::getEmailStr, actual,
          (expectedValue, actualValue) -> actualValue.trim().endsWith(expectedValue.trim()));
    }
  }
  
  @Factory
  public static IsEqualToEmailModel equalToEmailModel(EmailModel email) {
    return new IsEqualToEmailModel(email);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInAnyOrder(EmailModel... emails) {
    return containsEntityInAnyOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInAnyOrder(Iterable<EmailModel> emails) {
    return containsEntityInAnyOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInAnyOrder(Stream<EmailModel> emails) {
    return containsEntityInAnyOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInOrder(EmailModel... emails) {
    return containsEntityInOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInOrder(Iterable<EmailModel> emails) {
    return containsEntityInOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
  
  @Factory
  public static Matcher<Iterable<? extends EmailModel>> containsEmailModelInOrder(Stream<EmailModel> emails) {
    return containsEntityInOrder(IsEqualToEmailModel::equalToEmailModel, emails);
  }
}
