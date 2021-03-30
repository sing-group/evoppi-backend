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
