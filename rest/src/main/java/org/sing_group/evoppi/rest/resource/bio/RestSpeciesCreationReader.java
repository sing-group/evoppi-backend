/*-
 * #%L
 * REST
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
package org.sing_group.evoppi.rest.resource.bio;

import java.io.File;

import javax.ws.rs.ext.Provider;

import org.sing_group.evoppi.rest.MultipartMessageBodyReader;
import org.sing_group.evoppi.rest.entity.bio.RestSpeciesCreationData;

@Provider
public class RestSpeciesCreationReader extends MultipartMessageBodyReader<RestSpeciesCreationData> {

  private String name;
  private String gbffGzipFileUrl;

  @Override
  protected void add(String name, String value) {
    switch (name) {
      case "name":
        this.name = value;
        break;
      case "gbffGzipFileUrl":
        this.gbffGzipFileUrl = value;
        break;
    }
  }

  @Override
  protected RestSpeciesCreationData build() {
    return new RestSpeciesCreationData(name, gbffGzipFileUrl);
  }

  @Override
  protected void add(String name, File uploadedFile) {}

  @Override
  protected void init() {
    this.name = null;
    this.gbffGzipFileUrl = null;
  }
}
