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
package org.sing_group.evoppi.service.bio.species;

import java.io.Serializable;

import org.sing_group.evoppi.service.bio.entity.SpeciesCreationData;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationConfiguration;

public class DefaultSpeciesCreationConfiguration implements SpeciesCreationConfiguration, Serializable {
  private static final long serialVersionUID = 1L;
  
  private SpeciesCreationData data;
  private String workId;

  public DefaultSpeciesCreationConfiguration(SpeciesCreationData data, String workId) {
    super();
    this.data = data;
    this.workId = workId;
  }

  @Override
  public SpeciesCreationData getData() {
    return this.data;
  }

  @Override
  public String getWorkId() {
    return this.workId;
  }
}
