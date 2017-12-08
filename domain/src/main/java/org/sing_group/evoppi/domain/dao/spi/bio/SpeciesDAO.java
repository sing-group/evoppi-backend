package org.sing_group.evoppi.domain.dao.spi.bio;

import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Species;

public interface SpeciesDAO {

  public Stream<Species> listSpecies();

  public Species getSpecie(int id);

}
