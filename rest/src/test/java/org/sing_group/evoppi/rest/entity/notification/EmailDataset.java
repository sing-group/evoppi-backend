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
package org.sing_group.evoppi.rest.entity.notification;

import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;

import com.github.sleroy.fakesmtp.model.EmailModel;

public class EmailDataset {
  private static String URI_PREFIX = "http://localhost:8080/evoppi/regitration/confirmation?uuid=";
  
  private EmailDataset() {}

  public static EmailModel emailForRegistration(UserRegistrationData registration, String uuid) {
    final EmailModel email = new EmailModel();
    
    email.setFrom("no-reply@evoppi.sing-group.org");
    email.setTo(registration.getEmail());
    email.setSubject("EvoPPI registration confirmation");
    email.setEmailStr(
      String.format(
        "<p>Dear %s,</p><p>In order to complete your EvoPPI registration, please access the follow "
        + "<a href=\"%s\">link</a>.</p><p>Sincerely,<br />"
        + "<br />&nbsp;&nbsp;EvoPPI Team</p>",
        registration.getLogin(),
        URI_PREFIX + uuid
      )      
    );
    
    return email;
  }
}
