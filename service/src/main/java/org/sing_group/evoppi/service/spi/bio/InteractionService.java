package org.sing_group.evoppi.service.spi.bio;

import java.util.stream.Stream;

import org.sing_group.evoppi.domain.entities.bio.Interaction;

public interface InteractionService {

  public Stream<Interaction> findInteractionsByGene(int geneId, int[] interactomes);
  
}
