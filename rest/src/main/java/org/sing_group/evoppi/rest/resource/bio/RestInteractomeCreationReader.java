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
import org.sing_group.evoppi.rest.entity.bio.RestDatabaseInteractomeCreationData;
import org.sing_group.interactomesparser.UniProtDb;

@Provider
public class RestInteractomeCreationReader extends MultipartMessageBodyReader<RestDatabaseInteractomeCreationData> {

  private File file;
  private String name;
  private Integer speciesDbId;
  private Integer interactomeCollectionId;
  private UniProtDb dbSource;
  private Integer geneColumn1;
  private Integer geneColumn2;
  private Integer headerLinesCount;
  private String genePrefix;
  private String geneSuffix;
  private Integer organismColumn1;
  private Integer organismColumn2;
  private String organismPrefix;
  private String organismSuffix;

  @Override
  protected void add(String name, String value) {
    switch (name) {
      case "name":
        this.name = value;
        break;
      case "speciesDbId":
        this.speciesDbId = Integer.valueOf(value);
        break;
      case "interactomeCollectionId":
        this.interactomeCollectionId = Integer.valueOf(value);
        break;
      case "dbSource":
        this.dbSource = UniProtDb.get(value).orElseThrow(() -> new RuntimeException("dbSource not valid"));
        break;
      case "geneColumn1":
        this.geneColumn1 = Integer.valueOf(value);
        break;
      case "geneColumn2":
        this.geneColumn2 = Integer.valueOf(value);
        break;
      case "headerLinesCount":
        this.headerLinesCount = Integer.valueOf(value);
        break;
      case "genePrefix":
        this.genePrefix = value;
        break;
      case "geneSuffix":
        this.geneSuffix = value;
        break;
      case "organismColumn1":
        this.organismColumn1 = Integer.valueOf(value);
        break;
      case "organismColumn2":
        this.organismColumn2 = Integer.valueOf(value);
        break;
      case "organismPrefix":
        this.organismPrefix = value;
        break;
      case "organismSuffix":
        this.organismSuffix = value;
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
  protected RestDatabaseInteractomeCreationData build() {
    return new RestDatabaseInteractomeCreationData(
      file, name, speciesDbId, interactomeCollectionId, dbSource, geneColumn1, geneColumn2, headerLinesCount, genePrefix,
      geneSuffix, organismColumn1, organismColumn2, organismPrefix, organismSuffix
    );
  }

  @Override
  protected void init() {
    this.file = null;
    this.name = null;
    this.speciesDbId = null;
    this.interactomeCollectionId = null;
    this.dbSource = null;
    this.geneColumn1 = null;
    this.geneColumn2 = null;
    this.headerLinesCount = null;
    this.genePrefix = null;
    this.geneSuffix = null;
    this.organismColumn1 = null;
    this.organismColumn2 = null;
    this.organismPrefix = null;
    this.organismSuffix = null;
  }
}
