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

import org.sing_group.evoppi.service.entity.bio.InteractomeCreationData;
import org.sing_group.evoppi.service.spi.bio.interactome.InteractomeCreationConfiguration;

public class DefaultInteractomeCreationConfiguration implements InteractomeCreationConfiguration, Serializable {
  private static final long serialVersionUID = 1L;
  
  private InteractomeCreationData data;
  private String workId;

  public DefaultInteractomeCreationConfiguration(InteractomeCreationData data, String workId) {
    super();
    this.data = data;
    this.workId = workId;
  }

  @Override
  public InteractomeCreationData getData() {
    return this.data;
  }

  @Override
  public String getWorkId() {
    return this.workId;
  }
}
