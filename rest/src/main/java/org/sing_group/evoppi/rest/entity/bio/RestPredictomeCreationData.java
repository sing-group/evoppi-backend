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
package org.sing_group.evoppi.rest.entity.bio;

import java.io.File;
import java.io.Serializable;

import org.sing_group.evoppi.service.bio.entity.PredictomeCreationData;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "predictome-upload-data", description = "Upload data of a predictome.")
public class RestPredictomeCreationData extends PredictomeCreationData implements Serializable {
  private static final long serialVersionUID = 1L;

  public RestPredictomeCreationData(
    File file, int speciesADbId, int speciesBDbId, String sourceInteractome, String conversionDatabase
  ) {
    super(
      file, speciesADbId, speciesBDbId, sourceInteractome, conversionDatabase
    );
  }
}