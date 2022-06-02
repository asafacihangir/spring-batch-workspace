package com.phoenix.springbatch;

import org.springframework.beans.BeanUtils;

public class TrackedOrder extends Order {

  private String trackingNumber;

  private boolean freeShipping;

  public TrackedOrder() {

  }

  public TrackedOrder(Order order) {
    BeanUtils.copyProperties(order, this);
  }

  public String getTrackingNumber() {
    return trackingNumber;
  }

  public void setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
  }

  public boolean isFreeShipping() {
    return freeShipping;
  }

  public void setFreeShipping(boolean freeShipping) {
    this.freeShipping = freeShipping;
  }

}