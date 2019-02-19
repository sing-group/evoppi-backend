/*-
 * #%L
 * Domain
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

package org.sing_group.evoppi.domain.dao.user;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.user.RegistrationDAO;
import org.sing_group.evoppi.domain.entities.user.Login;
import org.sing_group.evoppi.domain.entities.user.Registration;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultRegistrationDAO implements RegistrationDAO {

  @PersistenceContext
  protected EntityManager em;
  
  protected DAOHelper<Login, Registration> dh;
  
  DefaultRegistrationDAO() {}

  public DefaultRegistrationDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Login.class, Registration.class, this.em);
  }
  
  @Override
  public Stream<Registration> list() {
    return this.dh.list().stream();
  }

  @Override
  public Registration register(Registration registration) {
    return this.dh.persist(registration);
  }

  @Override
  public Registration confirm(String code) {
    try {
      final Registration registration = this.dh.getBy("code", code);
      
      this.dh.remove(registration);
      
      return registration;
    } catch (NoResultException nre) {
      throw new IllegalArgumentException("No registration with code: " + code, nre);
    }
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
}
