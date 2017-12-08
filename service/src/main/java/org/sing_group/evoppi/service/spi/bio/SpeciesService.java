package org.sing_group.evoppi.service.spi.bio;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.evoppi.domain.entities.bio.Species;

@Local
public interface SpeciesService {

  public Stream<Species> listSpecies();
  
}
