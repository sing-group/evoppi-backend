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

import static org.sing_group.fluent.checker.Checks.requireEmail;
import static org.sing_group.fluent.checker.Checks.requireMD5;
import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserCredentials implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "password", length = 32, columnDefinition = "CHAR(32)", nullable = false)
  private String password;

  @Column(name = "email", length = 100, columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
  private String email;
  
  UserCredentials() {}
  
  public UserCredentials(String email, String password) {
    this(email, password, true);
  }
  
  public UserCredentials(String email, String password, boolean encodePassword) {
    this.setEmail(email);
    
    if (password != null) {
      if (encodePassword) {
        this.changePassword(password);
      } else {
        this.setPassword(password);
      }
    }
  }

  /**
   * Returns the email of this user.
   * 
   * @return the email of this user.
   */
  public String getEmail() {
    return email;
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
    this.email = requireEmail(email, 100, "'email' should have email format and a length between 1 and 100");
  }
  
  /**
   * Returns the MD5 of the user's password. Capital letters are used in the
   * returned string.
   * 
   * @return the MD5 of the user's password. Capital letters are used in the
   *         returned string.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the MD5 of the user's password. The password will be stored using
   * capital letters.
   * 
   * @param password MD5 of the user's password.
   */
  public void setPassword(String password) {
    this.password = requireMD5(password, "'password' should be a valid MD5 string").toUpperCase();
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
    requireStringSize(password, 6, Integer.MAX_VALUE, "password can't be shorter than 6");

    try {
      final MessageDigest digester = MessageDigest.getInstance("MD5");
      final Function<byte[], String> bytesToHexString =
        digest -> new BigInteger(1, digest).toString(16).toUpperCase(); 
      
      this.password = bytesToHexString.apply(digester.digest(password.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5 algorithm not found", e);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
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
    UserCredentials other = (UserCredentials) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    return true;
  }
}
