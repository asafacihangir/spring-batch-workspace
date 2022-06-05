package com.phoenix.springbatch.service.model;

import java.math.BigDecimal;

public class Person {

  private String name;
  private String birthday;
  private String email;
  private BigDecimal revenue;
  private Boolean customer;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public BigDecimal getRevenue() {
    return revenue;
  }

  public void setRevenue(BigDecimal revenue) {
    this.revenue = revenue;
  }

  public Boolean getCustomer() {
    return customer;
  }

  public void setCustomer(Boolean customer) {
    this.customer = customer;
  }
}
