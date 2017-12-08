package org.sing_group.evoppi.rest.entity.mapper.spi.bio;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;

public interface BioMapper {
  public SpeciesData toSpeciesData(Species species, UriBuilder uriBuilder);
  
  public InteractomeData toInteractomeData(Interactome interactome, UriBuilder uriBuilder);
}
