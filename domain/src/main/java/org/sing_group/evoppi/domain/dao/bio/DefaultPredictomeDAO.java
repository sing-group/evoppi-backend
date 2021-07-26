package org.sing_group.evoppi.domain.dao.bio;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.PredictomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Predictome;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPredictomeDAO implements PredictomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Predictome> dh;

  public DefaultPredictomeDAO() {
    super();
  }

  public DefaultPredictomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Predictome.class, this.em);
  }

  @Override
  public Stream<Predictome> list(ListingOptions<Predictome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public Predictome get(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown predictome: " + id));
  }

  @Override
  public long count(ListingOptions<Predictome> interactomeListingOptions) {
    return this.dh.count(interactomeListingOptions);
  }
}
