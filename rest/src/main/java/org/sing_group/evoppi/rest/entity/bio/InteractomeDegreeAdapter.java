/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */



package org.sing_group.evoppi.rest.entity.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.sing_group.evoppi.rest.entity.bio.InteractomeDegreeAdapter.InteractomeDegreeList;

public class InteractomeDegreeAdapter extends XmlAdapter<InteractomeDegreeList, Map<Integer, Integer>> {
  @Override
  public Map<Integer, Integer> unmarshal(InteractomeDegreeList v) throws Exception {
    return v.toMap();
  }

  @Override
  public InteractomeDegreeList marshal(Map<Integer, Integer> v) throws Exception {
    return new InteractomeDegreeList(v);
  }
  
  @XmlRootElement(name = "map", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class InteractomeDegreeList extends ArrayList<InteractomeDegree> {
    private static final long serialVersionUID = 1L;
    
    public InteractomeDegreeList() {}
    
    public InteractomeDegreeList(Map<Integer, Integer> values) {
      values.entrySet().stream()
        .map(InteractomeDegree::new)
      .forEach(this::add);
    }
    
    public Map<Integer, Integer> toMap() {
      return this.stream()
        .collect(Collectors.toMap(
          InteractomeDegree::getInteractomeId,
          InteractomeDegree::getDegree
        ));
    }
  }


  @XmlRootElement(name = "entry", namespace = "http://entity.resource.rest.evoppi.sing-group.org")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class InteractomeDegree implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlAttribute(name = "id")
    private int interactomeId;
    
    @XmlAttribute(name = "degree")
    private int degree;
    
    public InteractomeDegree() {}
    
    public InteractomeDegree(int key, int value) {
      this.interactomeId = key;
      this.degree = value;
    }
    
    public InteractomeDegree(Map.Entry<Integer, Integer> entry) {
      this(entry.getKey(), entry.getValue());
    }
    
    public int getInteractomeId() {
      return interactomeId;
    }
    
    public void setInteractomeId(int key) {
      this.interactomeId = key;
    }
    
    public int getDegree() {
      return degree;
    }
    
    public void setDegree(int value) {
      this.degree = value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + interactomeId;
      result = prime * result + degree;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      InteractomeDegree other = (InteractomeDegree) obj;
      if (interactomeId != other.interactomeId)
        return false;
      if (degree != other.degree)
        return false;
      return true;
    }
  }
}
