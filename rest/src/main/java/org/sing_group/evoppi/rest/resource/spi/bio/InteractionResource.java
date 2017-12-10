package org.sing_group.evoppi.rest.resource.spi.bio;

import javax.ws.rs.core.Response;

public interface InteractionResource {
  public Response getInteractions(int geneId, int[] interactomes);
}
