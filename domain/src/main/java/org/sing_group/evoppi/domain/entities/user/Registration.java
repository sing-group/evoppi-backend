/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "registration")
public class Registration implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private Login login;
  
  @Embedded
  private UserCredentials credentials;

  @Column(name = "registrationDateTime", nullable = false)
  private LocalDateTime registrationDateTime;
  
  @Column(length = 36, columnDefinition = "CHAR(36)", nullable = false, unique = true)
  private String code;
  
  Registration() {}
  
  public Registration(String login, String email, String password) {
    this.login = new Login(login);
    this.credentials = new UserCredentials(email, password);
    
    this.registrationDateTime = LocalDateTime.now();
    this.code = UUID.randomUUID().toString();
  }

  public String getLogin() {
    return login.getLogin();
  }

  public String getEmail() {
    return this.credentials.getEmail();
  }

  public String getPassword() {
    return this.credentials.getPassword();
  }
  
  public LocalDateTime getRegistrationDateTime() {
    return registrationDateTime;
  }

  public String getCode() {
    return code;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((login == null) ? 0 : login.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Registration other = (Registration) obj;
    if (login == null) {
      if (other.login != null)
        return false;
    } else if (!login.equals(other.login))
      return false;
    return true;
  }
}
