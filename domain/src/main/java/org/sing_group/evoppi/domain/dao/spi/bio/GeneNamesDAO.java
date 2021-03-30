package org.sing_group.evoppi.domain.dao.spi.bio;

import java.util.Collection;

public interface GeneNamesDAO {
  public void removeMultipleByGeneId(Collection<Integer> geneIds);
}
