package org.sing_group.evoppi.domain.dao.bio;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.Species;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultSpeciesDAO implements SpeciesDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Species> dh;

  public DefaultSpeciesDAO() {
    super();
  }

  public DefaultSpeciesDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Species.class, this.em);
  }
  
  @Override
  public Stream<Species> listSpecies() {
    return this.dh.list().stream();
  }

}
