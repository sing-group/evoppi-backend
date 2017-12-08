package org.sing_group.evoppi.service.bio;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.service.spi.bio.InteractomeService;

@Stateless
@PermitAll
public class DefaultInteractomeService implements InteractomeService {
  @Inject
  private InteractomeDAO dao;
  
  @Override
  public Interactome getInteractome(int id) {
    return this.dao.getSpecie(id);
  }
}
