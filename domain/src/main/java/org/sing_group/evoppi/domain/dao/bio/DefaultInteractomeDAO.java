package org.sing_group.evoppi.domain.dao.bio;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractomeDAO implements InteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Interactome> dh;

  public DefaultInteractomeDAO() {
    super();
  }

  public DefaultInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Interactome.class, this.em);
  }

  @Override
  public Interactome getSpecie(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interactome: " + id));
  }

}
