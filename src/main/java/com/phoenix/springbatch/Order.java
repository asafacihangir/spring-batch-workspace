package com.phoenix.springbatch;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Pattern;

public class Order {

  private Long orderId;

  private String firstName;

  private String lastName;

  @Pattern(regexp = ".*\\.gov")
  private String email;

  private BigDecimal cost;

  private String itemId;

  private String itemName;

  private Date shipDate;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
        + ", cost=" + cost + ", itemId=" + itemId + ", itemName=" + itemName + ", shipDate=" + shipDate + "]";
  }

}
