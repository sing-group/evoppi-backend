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
package org.sing_group.evoppi.rest.resource.bio;

import java.io.File;

import javax.ws.rs.ext.Provider;

import org.sing_group.evoppi.rest.MultipartMessageBodyReader;
import org.sing_group.evoppi.rest.entity.bio.RestPredictomeCreationData;

@Provider
public class RestPredictomeCreationReader extends MultipartMessageBodyReader<RestPredictomeCreationData> {

  private File file;
  private Integer speciesADbId;
  private Integer speciesBDbId;
  private String sourceInteractome;
  private String conversionDatabase;
  private Integer interactomeCollectionId;

  @Override
  protected void add(String name, String value) {
    switch (name) {
      case "speciesADbId":
        this.speciesADbId = Integer.valueOf(value);
        break;
      case "speciesBDbId":
        this.speciesBDbId = Integer.valueOf(value);
        break;
      case "interactomeCollectionId":
        this.interactomeCollectionId = Integer.valueOf(value);
        break;
      case "sourceInteractome":
        this.sourceInteractome = value;
        break;
      case "conversionDatabase":
        this.conversionDatabase = value;
        break;
    }
  }

  @Override
  protected void add(String name, File uploadedFile) {
    switch (name) {
      case "file":
        this.file = uploadedFile;
        break;
    }
  }

  @Override
  protected RestPredictomeCreationData build() {
    return new RestPredictomeCreationData(
      file, speciesADbId, speciesBDbId, sourceInteractome, conversionDatabase, interactomeCollectionId
    );
  }

  @Override
  protected void init() {
    this.file = null;
    this.speciesADbId = null;
    this.speciesBDbId = null;
    this.sourceInteractome = null;
    this.conversionDatabase = null;
    this.interactomeCollectionId = null;
  }
}
