package org.sing_group.evoppi.domain.dao.bio;

import static java.util.stream.Collectors.joining;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneNamesDAO;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.GeneNames.GeneNamesId;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGeneNamesDAO implements GeneNamesDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<GeneNamesId, GeneNames> dh;

  public DefaultGeneNamesDAO() {
    super();
  }

  public DefaultGeneNamesDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(GeneNamesId.class, GeneNames.class, this.em);
  }

  @Override
  public void removeMultipleByGeneId(Collection<Integer> geneIds) {
    this.em.createNativeQuery(String.format(
      "DELETE FROM gene_name_value WHERE geneId IN (%s)", 
      geneIds.stream().map(i -> i.toString()).collect(joining(", "))
    )).executeUpdate();
    
    this.dh.removeMultipleByField("geneId", geneIds);
  }

}
