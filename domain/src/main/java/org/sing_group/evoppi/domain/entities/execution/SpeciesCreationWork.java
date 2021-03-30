package org.sing_group.evoppi.domain.entities.execution;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "work_species_creation")
public class SpeciesCreationWork extends WorkEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  SpeciesCreationWork() {}

  public SpeciesCreationWork(String name) {
    super(name);
  }
}
