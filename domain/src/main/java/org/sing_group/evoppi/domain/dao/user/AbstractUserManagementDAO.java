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
package org.sing_group.evoppi.domain.dao.user;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.entities.user.Login;
import org.sing_group.evoppi.domain.entities.user.User;

public abstract class AbstractUserManagementDAO<E extends User> implements UserManagementDAO<E> {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Login, E> dh;

  public AbstractUserManagementDAO() {
    super();
  }

  public AbstractUserManagementDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Login.class, this.getEntityClass(), this.em);
  }
  
  protected abstract Class<E> getEntityClass();

  @Override
  public E get(String login) {
    return this.dh.get(new Login(login))
      .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + login));
  }

  @Override
  public Stream<E> list(ListingOptions<E> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }
  
  @Override
  public long count(ListingOptions<E> listingOptions) {
    return this.dh.count(listingOptions);
  }

  @Override
  public E create(E user) {
    return this.dh.persist(user);
  }

  @Override
  public E update(E user) {
    final E persistentUser = this.get(user.getLogin());
    
    if (user.getPassword() != null)
      persistentUser.setPassword(user.getPassword());
    
    persistentUser.setEmail(user.getEmail());
    
    return persistentUser;
  }

  @Override
  public void delete(String login) {
    this.dh.remove(this.get(login));
  }

}
