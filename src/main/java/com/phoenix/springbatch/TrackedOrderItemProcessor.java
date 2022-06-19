package com.phoenix.springbatch;

import java.util.UUID;

import org.springframework.batch.item.ItemProcessor;

public class TrackedOrderItemProcessor implements ItemProcessor<Order, TrackedOrder> {

  @Override
  public TrackedOrder process(Order item) throws Exception {
    System.out.println("Processing order with id: " + item.getOrderId());
    TrackedOrder trackedOrder = new TrackedOrder(item);
    trackedOrder.setTrackingNumber(this.getTrackingNumber());
    return trackedOrder;
  }

  private String getTrackingNumber() throws OrderProcessingException {

    if(Math.random() < .03) {
      throw new OrderProcessingException();
    }

    return UUID.randomUUID().toString();
  }

}
