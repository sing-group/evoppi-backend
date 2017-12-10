package org.sing_group.evoppi.service.bio;

import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interaction;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

@Stateless
@PermitAll
public class DefaultInteractionService implements InteractionService {
  @Inject
  private GeneDAO geneDao;

  @Override
  public Stream<Interaction> findInteractionsByGene(int geneId, int[] interactomes) {
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    final Gene gene = this.geneDao.getGene(geneId);
    
    final Set<Integer> interactomesIds = IntStream.of(interactomes)
      .boxed()
    .collect(Collectors.toSet());
    
    return gene.getInteractsWith()
      .filter(interaction -> interactomesIds.contains(interaction.getInteractome().getId()));
  }

}
