package org.sing_group.evoppi.domain.dao.bio;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneSequenceDAO;
import org.sing_group.evoppi.domain.entities.bio.GeneSequence;
import org.sing_group.evoppi.domain.entities.bio.GeneSequence.GeneSequenceId;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGeneSequenceDAO implements GeneSequenceDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<GeneSequenceId, GeneSequence> dh;

  public DefaultGeneSequenceDAO() {
    super();
  }

  public DefaultGeneSequenceDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(GeneSequenceId.class, GeneSequence.class, this.em);
  }

  @Override
  public void removeMultipleByGeneId(Collection<Integer> geneIds) {
    this.dh.removeMultipleByField("gene", geneIds);
  }

}
