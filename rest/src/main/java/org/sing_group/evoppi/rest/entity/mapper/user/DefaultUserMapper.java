/*-
 * #%L
 * REST
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

package org.sing_group.evoppi.rest.entity.mapper.user;

import javax.enterprise.inject.Default;

import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.domain.entities.user.Registration;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.evoppi.rest.entity.user.AdministratorData;
import org.sing_group.evoppi.rest.entity.user.AdministratorEditionData;
import org.sing_group.evoppi.rest.entity.user.InteractionResultLinkageData;
import org.sing_group.evoppi.rest.entity.user.ResearcherData;
import org.sing_group.evoppi.rest.entity.user.ResearcherEditionData;
import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;
import org.sing_group.evoppi.service.user.entity.InteractionResultLinkage;

@Default
public class DefaultUserMapper implements UserMapper {
  @Override
  public AdministratorData toAdministratorData(Administrator admin) {
    return new AdministratorData(
      admin.getLogin(),
      admin.getEmail()
    );
  }

  @Override
  public AdministratorEditionData toAdministratorEditionData(Administrator admin, String password) {
    return new AdministratorEditionData(
      admin.getLogin(),
      password,
      admin.getEmail()
    );
  }

  @Override
  public ResearcherData toResearcherData(Researcher researcher) {
    return new ResearcherData(researcher.getLogin(), researcher.getEmail());
  }

  @Override
  public ResearcherEditionData toResearcherEditionData(Researcher researcher, String password) {
    return new ResearcherEditionData(
      researcher.getLogin(),
      password,
      researcher.getEmail()
    );
  }

  @Override
  public Administrator toAdministrator(AdministratorEditionData data) {
    return new Administrator(data.getLogin(), data.getEmail(), data.getPassword());
  }

  @Override
  public Researcher toResearcher(ResearcherEditionData data) {
    return new Researcher(data.getLogin(), data.getEmail(), data.getPassword());
  }

  @Override
  public Registration toRegistration(UserRegistrationData registration) {
    return new Registration(
      registration.getLogin(),
      registration.getEmail(),
      registration.getPassword()
    );
  }
  
  @Override
  public InteractionResultLinkageData toInteractionResultLinkageData(InteractionResultLinkage linkageResult) {
    return new InteractionResultLinkageData(
      linkageResult.getLinkedUuids().toArray(String[]::new),
      linkageResult.getLinkageFailedUuids().toArray(String[]::new)
    );
  }
}
