package org.sing_group.evoppi.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class SpeciesRestPathBuilder implements RestPathBuilder {
  protected UriBuilder builder;

  public SpeciesRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("species");
  }
  
  public SpeciesRestPathBuilder(UriBuilder builder, int id) {
    this(builder);
    
    this.builder = this.builder.path("species").path(Integer.toString(id));
  }

  @Override
  public URI build() {
    return this.builder.clone().build();
  }
}
