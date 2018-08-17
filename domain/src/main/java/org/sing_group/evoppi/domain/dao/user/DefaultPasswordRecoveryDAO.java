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
package org.sing_group.evoppi.domain.dao.user;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.user.PasswordRecoveryDAO;
import org.sing_group.evoppi.domain.entities.user.Login;
import org.sing_group.evoppi.domain.entities.user.PasswordRecovery;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPasswordRecoveryDAO implements PasswordRecoveryDAO {

  @PersistenceContext
  protected EntityManager em;
  
  protected DAOHelper<Login, PasswordRecovery> dh;
  
  DefaultPasswordRecoveryDAO() {}

  public DefaultPasswordRecoveryDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Login.class, PasswordRecovery.class, this.em);
  }

  @Override
  public PasswordRecovery register(PasswordRecovery recovery) {
    this.dh.get(new Login(recovery.getLogin()))
      .ifPresent(this.dh::remove);
    
    return this.dh.persist(recovery);
  }

  @Override
  public PasswordRecovery confirm(String code, String password) {
    try {
      final PasswordRecovery recovery = this.dh.getBy("code", code);
      recovery.getUser().changePassword(password);
      
      this.dh.remove(recovery);
      
      return recovery;
    } catch (NoResultException nre) {
      throw new IllegalArgumentException("No password recovery with code: " + code, nre);
    }
  }

}
