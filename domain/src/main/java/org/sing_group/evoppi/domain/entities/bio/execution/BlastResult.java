/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
package org.sing_group.evoppi.domain.entities.bio.execution;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blast_result")
public class BlastResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;
  
  private int qseqid;
  private int qseqversion;
  private int sseqid;
  private int sseqversion;
  
  @Column(name = "pident", scale = 3, precision = 3)
  private double pident;
  private int length;
  private int mismatch;
  private int gapopen;
  private int qstart;
  private int qend;
  private int sstart;
  private int send;
  
  @Column(name = "evalue")
  private String evalue;
  
  @Column(name = "bitscore", scale = 6, precision = 3)
  private double bitscore;

  BlastResult() {}
  
  public BlastResult(
    int qseqid, int qseqversion, int sseqid, int sseqversion,
    double pident, int length, int mismatch, int gapopen, int qstart,
    int qend, int sstart, int send, BigDecimal evalue, double bitscore
  ) {
    this.qseqid = qseqid;
    this.qseqversion = qseqversion;
    this.sseqid = sseqid;
    this.sseqversion = sseqversion;
    this.pident = pident;
    this.length = length;
    this.mismatch = mismatch;
    this.gapopen = gapopen;
    this.qstart = qstart;
    this.qend = qend;
    this.sstart = sstart;
    this.send = send;
    this.evalue = evalue.toString();
    this.bitscore = bitscore;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getQseqid() {
    return qseqid;
  }

  public void setQseqid(int qseqid) {
    this.qseqid = qseqid;
  }

  public int getQseqversion() {
    return qseqversion;
  }

  public void setQseqversion(int qseqversion) {
    this.qseqversion = qseqversion;
  }

  public int getSseqid() {
    return sseqid;
  }

  public void setSseqid(int sseqid) {
    this.sseqid = sseqid;
  }

  public int getSseqversion() {
    return sseqversion;
  }

  public void setSseqversion(int sseqversion) {
    this.sseqversion = sseqversion;
  }

  public double getPident() {
    return pident;
  }

  public void setPident(double pident) {
    this.pident = pident;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getMismatch() {
    return mismatch;
  }

  public void setMismatch(int mismatch) {
    this.mismatch = mismatch;
  }

  public int getGapopen() {
    return gapopen;
  }

  public void setGapopen(int gapopen) {
    this.gapopen = gapopen;
  }

  public int getQstart() {
    return qstart;
  }

  public void setQstart(int qstart) {
    this.qstart = qstart;
  }

  public int getQend() {
    return qend;
  }

  public void setQend(int qend) {
    this.qend = qend;
  }

  public int getSstart() {
    return sstart;
  }

  public void setSstart(int sstart) {
    this.sstart = sstart;
  }

  public int getSend() {
    return send;
  }

  public void setSend(int send) {
    this.send = send;
  }

  public BigDecimal getEvalue() {
    return new BigDecimal(evalue);
  }

  public void setEvalue(BigDecimal evalue) {
    this.evalue = evalue.toString();
  }

  public double getBitscore() {
    return bitscore;
  }

  public void setBitscore(double bitscore) {
    this.bitscore = bitscore;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(bitscore);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((evalue == null) ? 0 : evalue.hashCode());
    result = prime * result + gapopen;
    result = prime * result + id;
    result = prime * result + length;
    result = prime * result + mismatch;
    temp = Double.doubleToLongBits(pident);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + qend;
    result = prime * result + qseqid;
    result = prime * result + qseqversion;
    result = prime * result + qstart;
    result = prime * result + send;
    result = prime * result + sseqid;
    result = prime * result + sseqversion;
    result = prime * result + sstart;
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
    BlastResult other = (BlastResult) obj;
    if (Double.doubleToLongBits(bitscore) != Double.doubleToLongBits(other.bitscore))
      return false;
    if (evalue == null) {
      if (other.evalue != null)
        return false;
    } else if (!evalue.equals(other.evalue))
      return false;
    if (gapopen != other.gapopen)
      return false;
    if (id != other.id)
      return false;
    if (length != other.length)
      return false;
    if (mismatch != other.mismatch)
      return false;
    if (Double.doubleToLongBits(pident) != Double.doubleToLongBits(other.pident))
      return false;
    if (qend != other.qend)
      return false;
    if (qseqid != other.qseqid)
      return false;
    if (qseqversion != other.qseqversion)
      return false;
    if (qstart != other.qstart)
      return false;
    if (send != other.send)
      return false;
    if (sseqid != other.sseqid)
      return false;
    if (sseqversion != other.sseqversion)
      return false;
    if (sstart != other.sstart)
      return false;
    return true;
  }

}
