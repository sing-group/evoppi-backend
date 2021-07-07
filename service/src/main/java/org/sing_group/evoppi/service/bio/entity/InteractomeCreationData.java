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

import java.io.File;
import java.util.Objects;

import org.sing_group.interactomesparser.UniProtDb;

public class InteractomeCreationData {

  private File file;
  private String name;

  private Integer speciesDbId;

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

  public InteractomeCreationData(
    File file, String name, Integer speciesDbId, UniProtDb dbSource, Integer geneColumn1, Integer geneColumn2,
    Integer headerLinesCount, String genePrefix, String geneSuffix, Integer organismColumn1, Integer organismColumn2,
    String organismPrefix, String organismSuffix
  ) {
    this.file = Objects.requireNonNull(file, "The interactome file is mandatory");
    this.name = Objects.requireNonNull(name, "The interactome name is mandatory");
    this.speciesDbId = Objects.requireNonNull(speciesDbId, "The species ID is mandatory");
    this.dbSource = Objects.requireNonNull(dbSource, "The DB source ID is mandatory");
    this.geneColumn1 = Objects.requireNonNull(geneColumn1, "The gene column 1 is mandatory");
    this.geneColumn2 = Objects.requireNonNull(geneColumn2, "The gene column 2 is mandatory");
    this.headerLinesCount = headerLinesCount;
    this.genePrefix = genePrefix;
    this.geneSuffix = geneSuffix;
    this.organismColumn1 = organismColumn1;
    this.organismColumn2 = organismColumn2;
    this.organismPrefix = organismPrefix;
    this.organismSuffix = organismSuffix;
    this.checkCreationData();
  }

  private void checkCreationData() {
    if (geneColumn1.equals(geneColumn2)) {
      throw new IllegalArgumentException("geneColumn1 and geneColumn2 must be different");
    }

    if ((organismColumn1 != null && organismColumn2 == null) || (organismColumn1 == null && organismColumn2 != null)) {
      throw new IllegalArgumentException("organismColumn1 and organismColumn2 must be used together");
    }
  }

  public File getFile() {
    return file;
  }

  public String getName() {
    return name;
  }

  public Integer getSpeciesDbId() {
    return speciesDbId;
  }

  public UniProtDb getDbSource() {
    return dbSource;
  }

  public Integer getGeneColumn1() {
    return geneColumn1;
  }

  public Integer getGeneColumn2() {
    return geneColumn2;
  }

  public Integer getHeaderLinesCount() {
    return headerLinesCount;
  }

  public String getGenePrefix() {
    return genePrefix;
  }

  public String getGeneSuffix() {
    return geneSuffix;
  }

  public Integer getOrganismColumn1() {
    return organismColumn1;
  }

  public Integer getOrganismColumn2() {
    return organismColumn2;
  }

  public String getOrganismPrefix() {
    return organismPrefix;
  }

  public String getOrganismSuffix() {
    return organismSuffix;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("InteractomeCreationData:");

    sb.append("\n\tfile = ").append(file.getName())
      .append("\n\tname = ").append(name)
      .append("\n\tspeciesDbId = ").append(speciesDbId)
      .append("\n\tdbSource = ").append(dbSource)
      .append("\n\tgeneColumn1 = ").append(geneColumn1)
      .append("\n\tgeneColumn2 = ").append(geneColumn2)
      .append("\n\theaderLinesCount = ").append(headerLinesCount)
      .append("\n\tgenePrefix = ").append(genePrefix)
      .append("\n\tgeneSuffix = ").append(geneSuffix)
      .append("\n\torganismColumn1 = ").append(organismColumn1)
      .append("\n\torganismColumn2 = ").append(organismColumn2)
      .append("\n\torganismPrefix = ").append(organismPrefix)
      .append("\n\torganismSuffix = ").append(organismSuffix);

    return sb.toString();
  }
}
