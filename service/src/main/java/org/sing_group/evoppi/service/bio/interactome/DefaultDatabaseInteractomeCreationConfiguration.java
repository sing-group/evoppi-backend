/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2021 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.bio.interactome;

import java.io.Serializable;

import org.sing_group.evoppi.service.bio.entity.DatabaseInteractomeCreationData;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationConfiguration;

public class DefaultDatabaseInteractomeCreationConfiguration
  implements DatabaseInteractomeCreationConfiguration, Serializable {
  private static final long serialVersionUID = 1L;

  private DatabaseInteractomeCreationData data;
  private String workId;

  public DefaultDatabaseInteractomeCreationConfiguration(DatabaseInteractomeCreationData data, String workId) {
    super();
    this.data = data;
    this.workId = workId;
  }

  @Override
  public DatabaseInteractomeCreationData getData() {
    return this.data;
  }

  @Override
  public String getWorkId() {
    return this.workId;
  }
}
