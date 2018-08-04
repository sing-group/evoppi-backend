/*-
 * #%L
 * REST
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



package org.sing_group.evoppi.rest.entity.mapper.spi.user;

import org.sing_group.evoppi.domain.entities.user.Administrator;
import org.sing_group.evoppi.domain.entities.user.Registration;
import org.sing_group.evoppi.domain.entities.user.Researcher;
import org.sing_group.evoppi.rest.entity.user.AdministratorData;
import org.sing_group.evoppi.rest.entity.user.AdministratorEditionData;
import org.sing_group.evoppi.rest.entity.user.InteractionResultLinkageData;
import org.sing_group.evoppi.rest.entity.user.ResearcherData;
import org.sing_group.evoppi.rest.entity.user.ResearcherEditionData;
import org.sing_group.evoppi.rest.entity.user.UserRegistrationData;
import org.sing_group.evoppi.service.user.entity.InteractionResultLinkage;

public interface UserMapper {
  public Administrator toAdministrator(AdministratorEditionData data);

  public AdministratorData toAdministratorData(Administrator admin);

  public AdministratorEditionData toAdministratorEditionData(Administrator admin, String password);
  
  public Researcher toResearcher(ResearcherEditionData data);

  public ResearcherData toResearcherData(Researcher researcher);

  public ResearcherEditionData toResearcherEditionData(Researcher researcher, String password);

  public Registration toRegistration(UserRegistrationData registration);
  
  public InteractionResultLinkageData toInteractionResultLinkageData(InteractionResultLinkage linkageResult);
}
