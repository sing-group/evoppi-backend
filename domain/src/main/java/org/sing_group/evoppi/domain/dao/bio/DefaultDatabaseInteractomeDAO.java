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
import org.sing_group.evoppi.domain.dao.spi.bio.DatabaseInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultDatabaseInteractomeDAO implements DatabaseInteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, DatabaseInteractome> dh;

  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private GeneInInteractomeDAO geneInInteractomeDao;
  
  public DefaultDatabaseInteractomeDAO() {
    super();
  }

  public DefaultDatabaseInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, DatabaseInteractome.class, this.em);
  }

  @Override
  public Stream<DatabaseInteractome> list(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public DatabaseInteractome get(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown database interactome: " + id));
  }

  @Override
  public long count(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dh.count(listingOptions);
  }
  
  @Override
  public DatabaseInteractome create(
    String name, String dbSourceIdType, Integer numOriginalInteractions, Integer numUniqueOriginalInteractions,
    Integer numUniqueOriginalGenes, Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    Species species, GeneInteractions interactions
  ) {
    DatabaseInteractome interactome =
      this.dh.persist(
        new DatabaseInteractome(
          name, dbSourceIdType, numOriginalInteractions, numUniqueOriginalInteractions, numUniqueOriginalGenes,
          numInteractionsNotToUniProtKB, numGenesNotToUniProtKB, numInteractionsNotToGeneId, numGenesNotToGeneId,
          numFinalInteractions, numFinalGenes, numRemovedInterSpeciesInteractions, numMultimappedToGeneId,
          species, species
        )
      );

    Map<Integer, Gene> geneMap = new HashMap<>();
    Map<Gene, GeneInInteractome> geneInInteractomeMap = new HashMap<>();

    interactions.getGenes().forEach(gene -> {
      try {
        Gene geneEntity = geneDao.getGene(gene);
        GeneInInteractome gi = this.geneInInteractomeDao.create(species, interactome, geneEntity);

        geneMap.put(gene, geneEntity);
        geneInInteractomeMap.put(geneEntity, gi);
      } catch (IllegalArgumentException e) {}
    });

    interactions.forEach(interaction -> {
      Gene geneA = geneMap.get(interaction.getA());
      Gene geneB = geneMap.get(interaction.getB());

      if (geneA != null && geneB != null) {
        interactome.addInteraction(species, species, geneA, geneB, geneInInteractomeMap.get(geneA), geneInInteractomeMap.get(geneB));
      }
    });

    geneMap.clear();

    return interactome;
  }
}
