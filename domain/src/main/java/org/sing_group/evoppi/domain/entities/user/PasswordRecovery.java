/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.user;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "password_recovery")
public class PasswordRecovery implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private Login login;
  
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "login", referencedColumnName = "login", nullable = false)
  private User user;

  @Column(length = 36, columnDefinition = "CHAR(36)", nullable = false, unique = true)
  private String code;

  PasswordRecovery() {}

  public PasswordRecovery(User user) {
    this.user = requireNonNull(user);
    this.login = new Login(user.getLogin());
    this.code = UUID.randomUUID().toString();
  }
  
  public String getLogin() {
    return this.login.getLogin();
  }

  public User getUser() {
    return user;
  }

  public String getCode() {
    return code;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
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
    PasswordRecovery other = (PasswordRecovery) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (!user.equals(other.user))
      return false;
    return true;
  }

}
