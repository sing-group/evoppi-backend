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
package org.sing_group.evoppi.service.bio.entity;

import java.util.Objects;

public class SpeciesCreationData {

  private String name;
  private String gbffGzipFileUrl;

  public SpeciesCreationData(String name, String gbffGzipFileUrl) {
    this.name = Objects.requireNonNull(name, "The species name is mandatory");
    this.gbffGzipFileUrl = Objects.requireNonNull(gbffGzipFileUrl, "The URL to the gzip GBFF file is mandatory");
  }

  public String getName() {
    return name;
  }

  public String getGbffGzipFileUrl() {
    return gbffGzipFileUrl;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SpeciesCreationData:");

    sb.append("\n\tname =").append(name)
      .append("\n\turl =").append(gbffGzipFileUrl);

    return sb.toString();
  }
}
