package com.phoenix.springbatch.job;

import com.phoenix.springbatch.domain.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

  @Override
  public Customer process(Customer customer) throws Exception {
    return customer;
  }
}