/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

package org.sing_group.evoppi.rest.entity.user;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(
  name = "interaction-result-linkage-data", namespace = "http://entity.resource.rest.evoppi.sing-group.org"
)
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(
  value = "interaction-result-linkage-data", description = "Result of linking interaction results to an user. "
    + "This entity includes the UUIDs of the results successfully and unsuccessfully linked to an user."
)
public class InteractionResultLinkageData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String[] linkedUuids;
  private String[] linkageFailedUuids;

  InteractionResultLinkageData() {}
  
  public InteractionResultLinkageData(String[] linkedUuids, String[] linkageFailedUuids) {
    this.linkedUuids = linkedUuids;
    this.linkageFailedUuids = linkageFailedUuids;
  }

  public String[] getLinkedUuids() {
    return linkedUuids;
  }

  public void setLinkedUuids(String[] linkedUuids) {
    this.linkedUuids = linkedUuids;
  }

  public String[] getLinkageFailedUuids() {
    return linkageFailedUuids;
  }

  public void setLinkageFailedUuids(String[] linkageFailedUuids) {
    this.linkageFailedUuids = linkageFailedUuids;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(linkageFailedUuids);
    result = prime * result + Arrays.hashCode(linkedUuids);
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
    InteractionResultLinkageData other = (InteractionResultLinkageData) obj;
    if (!Arrays.equals(linkageFailedUuids, other.linkageFailedUuids))
      return false;
    if (!Arrays.equals(linkedUuids, other.linkedUuids))
      return false;
    return true;
  }

}
