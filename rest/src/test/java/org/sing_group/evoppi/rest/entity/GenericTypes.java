/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.entity;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.rest.entity.user.AdministratorData;
import org.sing_group.evoppi.rest.entity.user.ResearcherData;

/**
 * This class have methods to read list of entities
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public final class GenericTypes {
  private GenericTypes() {}

  public static class AdministratorDataListType extends GenericType<List<AdministratorData>> {
    public static AdministratorDataListType ADMINISTRATOR_DATA_LIST_TYPE = new AdministratorDataListType();

    public static List<AdministratorData> readEntity(Response response) {
      return response.readEntity(ADMINISTRATOR_DATA_LIST_TYPE);
    }
  }
  
  public static class ResearcherDataListType extends GenericType<List<ResearcherData>> {
    public static ResearcherDataListType RESEARCHER_DATA_LIST_TYPE = new ResearcherDataListType();
    
    public static List<ResearcherData> readEntity(Response response) {
      return response.readEntity(RESEARCHER_DATA_LIST_TYPE);
    }
  }
  
}
