/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.user;

import java.security.Principal;
import java.util.Optional;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.user.PasswordRecoveryDAO;
import org.sing_group.evoppi.domain.dao.spi.user.RegistrationDAO;
import org.sing_group.evoppi.domain.dao.spi.user.ResearcherDAO;
import org.sing_group.evoppi.domain.dao.spi.user.UserDAO;
import org.sing_group.evoppi.domain.entities.user.PasswordRecovery;
import org.sing_group.evoppi.domain.entities.user.Registration;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.domain.entities.user.User;
import org.sing_group.evoppi.service.spi.notification.Mailer;
import org.sing_group.evoppi.service.spi.user.UserService;

@Stateless
@Default
@PermitAll
public class DefaultUserService implements UserService {

  @Inject
  private UserDAO userDao;

  @Inject
  private ResearcherDAO researcherDao;

  @Inject
  private RegistrationDAO registrationDao;
  
  @Inject
  private PasswordRecoveryDAO passwordRecoveryDao;

  @Inject
  private Principal principal;

  @Inject
  private Mailer mailer;

  @Resource(name = "java:global/evoppi/email/from")
  private String emailFrom;

  @Resource(name = "java:global/evoppi/email/registration/confirmationUrl")
  private String emailConfirmationUrl;

  @Resource(name = "java:global/evoppi/email/registration/subject")
  private String emailConfirmationSubject;

  @Resource(name = "java:global/evoppi/email/registration/message")
  private String emailConfirmationMessage;

  @Resource(name = "java:global/evoppi/email/passwordrecovery/confirmationUrl")
  private String emailPasswordRecoveryUrl;

  @Resource(name = "java:global/evoppi/email/passwordrecovery/subject")
  private String emailPasswordRecoverySubject;

  @Resource(name = "java:global/evoppi/email/passwordrecovery/message")
  private String emailPasswordRecoveryMessage;

  @Override
  @SuppressWarnings("unchecked")
  public <U extends User> Optional<U> getCurrentUser() {
    final String currentUserName = this.principal.getName();

    if ("anonymous".equals(currentUserName)) {
      return Optional.empty();
    } else {
      return Optional.of((U) this.userDao.get(currentUserName));
    }
  }

  @Override
  public void register(Registration registration) {
    if (this.userDao.exists(registration.getLogin()) || this.registrationDao.exists(registration.getLogin())) {
      throw new SecurityException("Login not available: " + registration.getLogin());
    } else if (
      this.userDao.existsEmail(registration.getEmail()) || this.registrationDao.existsEmail(registration.getEmail())
    ) {
      throw new SecurityException("Email not available: " + registration.getEmail());
    } else {
      registration = this.registrationDao.register(registration);

      this.mailer.sendEmail(
        this.emailFrom, registration.getEmail(), this.emailConfirmationSubject,
        this.composeConfirmationEmail(registration)
      );
    }
  }

  @Override
  public void confirm(String code) {
    final Registration registration = this.registrationDao.confirm(code);

    final Researcher researcher =
      new Researcher(registration.getLogin(), registration.getEmail(), registration.getPassword(), false);

    this.researcherDao.create(researcher);
  }

  private String composeConfirmationEmail(Registration registration) {
    return this.emailConfirmationMessage
      .replaceAll("\\[USER\\]", registration.getLogin())
      .replaceAll("\\[CONFIRMATION_URL\\]", this.emailConfirmationUrl.replaceAll("\\[UUID\\]", registration.getCode()));
  }
  
  @Override
  public void changeCurrentUserPassword(String password) {
    final User user = this.getCurrentUser()
      .orElseThrow(() -> new SecurityException("A logged user is required to change password"));
    
    this.userDao.changePassword(user.getLogin(), password);
  }

  @Override
  public void allowPassowdRecovery(String login) {
    final PasswordRecovery recovery =
      this.passwordRecoveryDao.register(new PasswordRecovery(this.userDao.get(login)));

    this.mailer.sendEmail(
      this.emailFrom, recovery.getUser().getEmail(), this.emailPasswordRecoverySubject,
      this.composePasswordRecoveryEmail(recovery)
    );
  }

  @Override
  public void recoverPassword(String code, String newPassword) {
    this.passwordRecoveryDao.confirm(code, newPassword);
  }

  private String composePasswordRecoveryEmail(PasswordRecovery recovery) {
    return this.emailPasswordRecoveryMessage
      .replaceAll("\\[USER\\]", recovery.getUser().getLogin())
      .replaceAll("\\[PASSWORD_RECOVERY_URL\\]", this.emailPasswordRecoveryUrl.replaceAll("\\[UUID\\]", recovery.getCode()));
  }
}
