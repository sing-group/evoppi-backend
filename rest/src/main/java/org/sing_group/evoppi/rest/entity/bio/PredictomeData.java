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
package org.sing_group.evoppi.rest.entity.bio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.evoppi.domain.entities.bio.InteractomeType;
import org.sing_group.evoppi.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "predictome", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "predictome", description = "Data of a predictome entity."
)
public class PredictomeData extends InteractomeData {
  private static final long serialVersionUID = 1L;

  private String sourceInteractome;

  private String conversionDatabase;

  public PredictomeData(
    int id, String name, IdAndUri speciesA, IdAndUri speciesB,
    String sourceInteractome, String conversionDatabase
  ) {
    super(id, name, speciesA, speciesB, InteractomeType.PREDICTOME);

    this.sourceInteractome = sourceInteractome;
    this.conversionDatabase = conversionDatabase;
  }

  public String getSourceInteractome() {
    return sourceInteractome;
  }

  public void setSourceInteractome(String sourceInteractome) {
    this.sourceInteractome = sourceInteractome;
  }

  public String getConversionDatabase() {
    return conversionDatabase;
  }

  public void setConversionDatabase(String conversionDatabase) {
    this.conversionDatabase = conversionDatabase;
  }
}
