package org.sing_group.evoppi.domain.dao.bio;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.PredictomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPredictomeDAO implements PredictomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Predictome> dh;

  @Inject
  private GeneDAO geneDao;

  @Inject
  private GeneInInteractomeDAO geneInInteractomeDao;

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

  @Override
  public Predictome create(
    String name, Species speciesA, Species speciesB, String sourceInteractome, String conversionDatabase,
    GeneInteractions interactions
  ) {
    Predictome predictome =
      this.dh.persist(
        new Predictome(name, speciesA, speciesB, sourceInteractome, conversionDatabase)
      );

    Map<Integer, Gene> geneMap = new HashMap<>();
    Map<Gene, GeneInInteractome> geneInInteractomeMap = new HashMap<>();

    interactions.getAGenes().forEach(gene -> {
      processGene(gene, speciesA, predictome, geneMap, geneInInteractomeMap);
    });
    interactions.getBGenes().forEach(gene -> {
      processGene(gene, speciesB, predictome, geneMap, geneInInteractomeMap);
    });

    interactions.forEach(interaction -> {
      Gene geneA = geneMap.get(interaction.getA());
      Gene geneB = geneMap.get(interaction.getB());

      if (geneA != null && geneB != null) {
        predictome.addInteraction(
          speciesA, speciesB, geneA, geneB, geneInInteractomeMap.get(geneA), geneInInteractomeMap.get(geneB)
        );
      }
    });

    geneMap.clear();

    return predictome;
  }

  private void processGene(
    int geneId, Species species, Predictome predictome, Map<Integer, Gene> geneMap,
    Map<Gene, GeneInInteractome> geneInInteractomeMap
  ) {
    try {
      Gene geneEntity = geneDao.getGene(geneId);
      GeneInInteractome gi = this.geneInInteractomeDao.create(species, predictome, geneEntity);

      geneMap.put(geneId, geneEntity);
      geneInInteractomeMap.put(geneEntity, gi);
    } catch (IllegalArgumentException e) {}
  }
}
