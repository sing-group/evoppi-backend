package org.sing_group.evoppi.rest.entity.mapper.bio;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.entity.user.IdAndUri;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

public class DefaultBioMapper implements BioMapper {

  @Override
  public SpeciesData toSpeciesData(Species species, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return new SpeciesData(
      species.getId(),
      species.getName(),
      species.getInteractomes()
        .map(interactome -> new IdAndUri(
          interactome.getId(),
          pathBuilder.interactome(interactome).build()
        ))
      .toArray(IdAndUri[]::new)
    );
  }

  @Override
  public InteractomeData toInteractomeData(Interactome interactome, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return new InteractomeData(
      interactome.getId(),
      interactome.getName(),
      new IdAndUri(
        interactome.getSpecies().getId(),
        pathBuilder.species(interactome.getSpecies()).build()
      )
    );
  }

}
