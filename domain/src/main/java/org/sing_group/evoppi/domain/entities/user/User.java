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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static String getRoleName(User user) {
    return getRole(user).name();
  }
  
  public static RoleType getRole(User user) {
    final Class<? extends User> userClass = user.getClass();
    final DiscriminatorValue dvAnnotation = userClass.getAnnotation(DiscriminatorValue.class);
    
    return RoleType.valueOf(dvAnnotation.value());
  }
  
  public static boolean haveSameRole(User user1, User user2) {
    return getRole(user1).equals(getRole(user2));
  }

  @EmbeddedId
  private Login login;
  
  @Embedded
  private UserCredentials credentials;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
  private Set<WorkEntity> results;

  // For JPA
  User() {}
  
  public User(String login, String email, String password) {
    this(login, email, password, true);
  }
  
  public User(String login, String email, String password, boolean encodePassword) {
    this.login = new Login(login);
    this.credentials = new UserCredentials(email, password, encodePassword);
    
    this.results = new HashSet<>();
  }

  public String getLogin() {
    return login.getLogin();
  }
  
  void setLogin(String login) {
    this.login.setLogin(login);
  }

  /**
   * Returns the email of this user.
   * 
   * @return the email of this user.
   */
  public String getEmail() {
    return this.credentials.getEmail();
  }

  /**
   * Sets the email of this user.
   * 
   * @param email
   *          the email of the user. This parameter must be a non empty and non
   *          {@code null} string with a maximum length of 100 chars.
   * @throws NullPointerException
   *           if {@code null} is passed as parameter.
   * @throws IllegalArgumentException
   *           if the length of the string passed is not valid.
   */
  public void setEmail(String email) {
    this.credentials.setEmail(email);
  }

  /**
   * Returns the MD5 of the user's password. Capital letters are used in the
   * returned string.
   * 
   * @return the MD5 of the user's password. Capital letters are used in the
   *         returned string.
   */
  public String getPassword() {
    return this.credentials.getPassword();
  }

  public void setPassword(String password) {
    this.credentials.setPassword(password);
  }

  /**
   * Changes the password of the user. This method receives the raw value of
   * the password and stores it in MD5 format.
   * 
   * @param password
   *          the raw password of the user. This parameter must be a non
   *          {@code null} string with a minimum length of 6 chars.
   * 
   * @throws NullPointerException
   *           if the {@code password} is {@code null}.
   * @throws IllegalArgumentException
   *           if the length of the string passed is not valid.
   */
  public void changePassword(String password) {
    this.credentials.changePassword(password);
  }
  
  public void addResult(InteractionsResult result) {
    if (this.results.add(result)) {
      result.setOwner(this);
    }
  }
  
  public Stream<WorkEntity> getResults() {
    return this.results.stream()
      .filter(work -> work instanceof InteractionsResult);
  }
  
  public Stream<SameSpeciesInteractionsResult> getSameSpeciesResults() {
    return this.results.stream()
      .filter(result -> result instanceof SameSpeciesInteractionsResult)
      .map(result -> (SameSpeciesInteractionsResult) result);
  }
  
  public Stream<DifferentSpeciesInteractionsResult> getDifferentSpeciesResults() {
    return this.results.stream()
      .filter(result -> result instanceof DifferentSpeciesInteractionsResult)
      .map(result -> (DifferentSpeciesInteractionsResult) result);
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
    User other = (User) obj;
    if (login == null) {
      if (other.login != null)
        return false;
    } else if (!login.equals(other.login))
      return false;
    return true;
  }
}
