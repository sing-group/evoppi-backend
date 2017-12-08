package org.sing_group.evoppi.rest.resource.spi.bio;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface SpeciesResource {
  public Response listSpecies();
}
