package com.phoenix.springbatch;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Order, TrackedOrder> {

  @Override
  public void onSkipInRead(Throwable t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onSkipInWrite(TrackedOrder item, Throwable t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onSkipInProcess(Order item, Throwable t) {
    System.out.println("Skipping processing of item with id: " + item.getOrderId());

  }

}