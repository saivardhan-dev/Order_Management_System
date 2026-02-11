package com.microservices.shipping_service.model;

public class ShippingAddress {
  private String houseNum;
  private String city;
  private String state;
  private String zip;
  private String country;

  public String getHouseNum() { return houseNum; }
  public void setHouseNum(String houseNum) { this.houseNum = houseNum; }

  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }

  public String getState() { return state; }
  public void setState(String state) { this.state = state; }

  public String getZip() { return zip; }
  public void setZip(String zip) { this.zip = zip; }

  public String getCountry() { return country; }
  public void setCountry(String country) { this.country = country; }
}