package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;

import org.sing_group.evoppi.service.bio.entity.SpeciesCreationData;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "species-upload-data", description = "Upload data of an species.")
public class RestSpeciesCreationData extends SpeciesCreationData implements Serializable {
  private static final long serialVersionUID = 1L;

  public RestSpeciesCreationData(String name, String gbffGzipFileUrl) {
    super(name, gbffGzipFileUrl);
  }
}
