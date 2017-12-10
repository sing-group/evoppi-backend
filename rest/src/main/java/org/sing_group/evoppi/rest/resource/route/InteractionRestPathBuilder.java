package org.sing_group.evoppi.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class InteractionRestPathBuilder implements RestPathBuilder {
  protected UriBuilder builder;

  public InteractionRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("interaction");
  }

  @Override
  public URI build() {
    return this.builder.clone().build();
  }
}
