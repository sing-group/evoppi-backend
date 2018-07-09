/*-
 * #%L
 * Tests
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



package org.sing_group.evoppi.domain.entities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.evoppi.domain.dao.SortDirection;
import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.domain.entities.user.RoleType;
import org.sing_group.evoppi.domain.entities.user.User;

public class UsersDataset {
  public final static String ADMIN_HTTP_BASIC_AUTH = "Basic YWRtaW46YWRtaW5wYXNz";
  public final static String RESEARCHER_HTTP_BASIC_AUTH = "Basic cmVzZWFyY2hlcjpyZXNlYXJjaGVycGFzcw==";
  
  private final static Map<String, String> LOGIN_TO_PASSWORD = new HashMap<>();
  private final static Map<String, String> LOGIN_TO_NEW_PASSWORD = new HashMap<>();
  
  static {
    LOGIN_TO_PASSWORD.put("admin", "adminpass");
    LOGIN_TO_PASSWORD.put("admin2", "admin2pass");
    LOGIN_TO_PASSWORD.put("admin3", "admin3pass");
    LOGIN_TO_PASSWORD.put("admin4", "admin4pass");
    LOGIN_TO_PASSWORD.put("researcher", "researcherpass");
    LOGIN_TO_PASSWORD.put("researcher2", "researcher2pass");
    LOGIN_TO_PASSWORD.put("researcher3", "researcher3pass");
    LOGIN_TO_PASSWORD.put("researcher4", "researcher4pass");
    LOGIN_TO_PASSWORD.put("adminNew", "adminNewpass");
    LOGIN_TO_PASSWORD.put("researcherNew", "researcherNewpass");
    
    LOGIN_TO_NEW_PASSWORD.put("admin", "adminModifiedpass");
    LOGIN_TO_NEW_PASSWORD.put("researcher", "researcherModifiedpass");
  }
  
  public static Administrator admin() {
    return user("admin");
  }
  
  public static Researcher researcher() {
    return user("researcher");
  }

  public static Stream<User> users() {
    return Stream.of(
      new Administrator("admin", "admin@email.com", passwordOf("admin")),
      new Administrator("admin2", "admin2@email.com", passwordOf("admin2")),
      new Administrator("admin3", "admin3@email.com", passwordOf("admin3")),
      new Administrator("admin4", "admin4@email.com", passwordOf("admin4")),
      new Researcher("researcher", "researcher@email.com", passwordOf("researcher")),
      new Researcher("researcher2", "researcher2@email.com", passwordOf("researcher2")),
      new Researcher("researcher3", "researcher3@email.com", passwordOf("researcher3")),
      new Researcher("researcher4", "researcher4@email.com", passwordOf("researcher4"))
    );
  }
  
  @SuppressWarnings("unchecked")
  private static <U extends User> Stream<U> users(RoleType role) {
    return (Stream<U>) users().filter(user -> User.getRole(user).equals(role));
  }
  
  @SuppressWarnings("unchecked")
  public static <U extends User> U user(String login) {
    return (U) users()
      .filter(user -> user.getLogin().equals(login))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Invalid login: " + login));
  }

  public static String passwordOf(User user) {
    return passwordOf(user.getLogin());
  }

  public static String passwordOf(String login) {
    if (LOGIN_TO_PASSWORD.containsKey(login)) {
      return LOGIN_TO_PASSWORD.get(login);
    } else if (isInvalidLogin(login)) {
      return login + "pass";
    } else {
      throw new IllegalArgumentException("Invalid email: " + login);
    }
  }

  public static String newPasswordOf(User user) {
    return newPasswordOf(user.getLogin());
  }
  
  public static String newPasswordOf(String login) {
    if (LOGIN_TO_NEW_PASSWORD.containsKey(login)) {
      return LOGIN_TO_NEW_PASSWORD.get(login);
    } else {
      throw new IllegalArgumentException("Invalid user for new password: " + login);
    }
  }

  public static Stream<String> validLogins() {
    return users().map(User::getLogin);
  }

  public static Stream<String> invalidLogins() {
    return Stream.of("invalid1", "fake", "notvalid");
  }

  private static boolean isInvalidLogin(String login) {
    return invalidLogins().anyMatch(login::equals);
  }
  
  public static Stream<Administrator> admins() {
    return users(RoleType.ADMIN);
  }

  public static <T extends Comparable<T>> Stream<Administrator> admins(
    int start, int end, Function<Administrator, T> getter, SortDirection sort
  ) {
    return filterUsers(admins(), start, end, getter, sort);
  }

  public static Administrator newAdministrator() {
    return new Administrator("adminNew", "adminNew@email.com", passwordOf("adminNew"));
  }

  public static Administrator modifiedAdministrator() {
    return new Administrator("admin", "adminModified@email.com", newPasswordOf("admin"));
  }

  public static Administrator administratorToDelete() {
    return user("admin2");
  }

  public static Stream<Researcher> researchers() {
    return users(RoleType.RESEARCHER);
  }

  public static <T extends Comparable<T>> Stream<Researcher> researchers(
    int start, int end, Function<Researcher, T> getter, SortDirection sort
  ) {
    return filterUsers(researchers(), start, end, getter, sort);
  }

  public static Researcher newResearcher() {
    return new Researcher("researcherNew", "researcherNew@email.com", passwordOf("researcherNew"));
  }

  public static Researcher modifiedResearcher() {
    return new Researcher("researcher", "researcherModified@email.com", newPasswordOf("researcher"));
  }

  public static Researcher researcherToDelete() {
    return user("researcher");
  }

  public static long countAdmins() {
    return admins().count();
  }

  public static long countResearchers() {
    return researchers().count();
  }
  
  private static <U extends User, T extends Comparable<T>> Stream<U> filterUsers(
    Stream<U> users, int start, int end, Function<U, T> getter, SortDirection sort
  ) {
    final Comparator<T> compare = sort == SortDirection.ASCENDING
      ? (c1, c2) -> c1.compareTo(c2)
      : (c1, c2) -> -c1.compareTo(c2);
    
    return users
      .sorted((c1, c2) -> compare.compare(getter.apply(c1), getter.apply(c2)))
      .skip(start)
      .limit(end - start + 1);
  }
  
}
