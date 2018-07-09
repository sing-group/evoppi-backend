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



package org.sing_group.evoppi.rest.entity.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.user.RoleType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Profile data of the researcher entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "researcher-edition-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "researcher-edition-data", description = "Edition data of the researcher entity.")
public class ResearcherEditionData extends UserEditionData {
  private static final long serialVersionUID = 1L;
  
  ResearcherEditionData() {
    super();
  }
  
  public ResearcherEditionData(String login, String password, String email) {
    super(login, password, email, RoleType.RESEARCHER);
  }
  
  @ApiModelProperty(allowableValues = "RESEARCHER", required = true)
  @Override
  public RoleType getRole() {
    return super.getRole();
  }

  @Override
  public void setRole(RoleType role) {
    if (role != RoleType.RESEARCHER)
      throw new IllegalArgumentException("Invalid role. Only " + RoleType.RESEARCHER + " role is admitted");
  }

  @Override
  public String toString() {
    return "ResearcherEditionData [getPassword()=" + getPassword() + ", getLogin()=" + getLogin() + ", getEmail()="
      + getEmail() + "]";
  }
}
