package com.fbellotti.api_ws_spring.test.model;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class Address {

  private String postal;
  private String city;

  public Address(String postal, String city) {
    this.postal = postal;
    this.city = city;
  }

  public String getPostal() {
    return postal;
  }

  public String getCity() {
    return city;
  }
}
