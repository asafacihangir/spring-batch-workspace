package com.phoenix.springbatch;

import java.math.BigDecimal;

import org.springframework.batch.item.ItemProcessor;

public class FreeShippingItemProcessor implements ItemProcessor<TrackedOrder, TrackedOrder> {

  @Override
  public TrackedOrder process(TrackedOrder item) {

    item.setFreeShipping(item.getCost().compareTo(new BigDecimal("80")) > 0);

    return item.isFreeShipping() ? item:null;
  }

}

