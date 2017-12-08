package org.sing_group.evoppi.service.bio;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.service.spi.bio.SpeciesService;

@Stateless
@PermitAll
public class DefaultSpeciesService implements SpeciesService {
  @Inject
  private SpeciesDAO dao;
  
  @Override
  public Stream<Species> listSpecies() {
    return this.dao.listSpecies();
  }
}
