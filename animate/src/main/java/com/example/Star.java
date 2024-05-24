package com.example;

import java.util.Date;

public class Star {
  private String name;
  private float ra;
  private float dec;
  private float mag;
  private float alt;
  private float az;

  public Star(String name, float ra, float dec, float mag) {
    this.name = name;
    this.ra = ra;
    this.dec = dec;
    this.mag = mag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getRA() {
    return ra;
  }

  public void setRA(float ra) {
    this.ra = ra;
  }

  public float getDEC() {
    return dec;
  }

  public void setDEC(float dec) {
    this.dec = dec;
  }

  public float getMAG() {
    return mag;
  }

  public void setMAG(float mag) {
    this.mag = mag;
  }

  public float getALT() {
    return alt;
  }

  public void setALT(float alt) {
    this.alt = alt;
  }

  public float getAZ() {
    return az;
  }

  public void setAZ(float az) {
    this.az = az;
  }

  public void precess(float lat, float lon, Date date) {
    RaDecToAltAz altaz = new RaDecToAltAz(lat, lon, this.ra, this.dec, date);
    // altaz.raDecToAltAz(lat, lon);
    this.alt = (float) altaz.getAlt();
    this.az = (float) altaz.getAz();
  }

  @Override
  public String toString() {
    return "Star{" +
        "name='" + name + '\'' +
        ", ra=" + ra +
        ", dec=" + dec +
        ", mag=" + mag +
        ", az =" + az +
        ", alt =" + alt +
        '}';
  }
}