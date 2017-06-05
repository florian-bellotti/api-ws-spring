package com.fbellotti.api_ws_spring.test.model;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class Customer {

  private String firstName;
  private String lastName;
  private Address address;

  public Customer(String firstName, String lastName, Address address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public Address getAddress() {
    return address;
  }
}
