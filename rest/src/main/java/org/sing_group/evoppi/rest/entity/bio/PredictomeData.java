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
