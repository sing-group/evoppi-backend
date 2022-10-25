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

import java.io.File;
import java.io.Serializable;

import org.sing_group.evoppi.service.bio.entity.DatabaseInteractomeCreationData;
import org.sing_group.interactomesparser.UniProtDb;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "database-interactome-upload-data", description = "Upload data of a database interactome.")
public class RestDatabaseInteractomeCreationData extends DatabaseInteractomeCreationData implements Serializable {
  private static final long serialVersionUID = 1L;

  public RestDatabaseInteractomeCreationData(
    File file, String name, Integer speciesDbId, Integer interactomeCollectionId, UniProtDb dbSource,
    Integer geneColumn1, Integer geneColumn2, Integer headerLinesCount, String genePrefix, String geneSuffix,
    Integer organismColumn1, Integer organismColumn2, String organismPrefix, String organismSuffix
  ) {
    super(
      file, name, speciesDbId, interactomeCollectionId, dbSource, geneColumn1, geneColumn2, headerLinesCount,
      genePrefix, geneSuffix, organismColumn1, organismColumn2, organismPrefix, organismSuffix
    );
  }
}
