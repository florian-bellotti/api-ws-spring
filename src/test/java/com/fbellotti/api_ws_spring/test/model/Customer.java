package com.fbellotti.api_ws_spring.test.model;

import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class Customer {

  @Id
  private String identifier;
  private String firstName;
  private String lastName;
  private Address address;

  public Customer(String firstName, String lastName, Address address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
