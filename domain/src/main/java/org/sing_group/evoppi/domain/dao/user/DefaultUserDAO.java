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

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.user.UserDAO;
import org.sing_group.evoppi.domain.entities.user.Login;
import org.sing_group.evoppi.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultUserDAO implements UserDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Login, User> dh;

  DefaultUserDAO() {}

  public DefaultUserDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Login.class, User.class, this.em);
  }

  @Override
  public User get(String login) {
    return dh.get(new Login(login))
      .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + login));
  }

  @Override
  public boolean exists(String login) {
    return this.dh.get(new Login(login)).isPresent();
  }
  
  @Override
  public boolean existsEmail(String email) {
    try {
      this.dh.getBy("credentials.email", email);
      return true;
    } catch (NoResultException nre) {
      return false;
    }
  }
  
  @Override
  public void changePassword(String login, String password) {
    final User user = this.get(login);
    
    user.changePassword(password);
  }
}
