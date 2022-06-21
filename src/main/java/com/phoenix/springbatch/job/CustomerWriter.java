package com.phoenix.springbatch.job;

import com.phoenix.springbatch.domain.Customer;
import com.phoenix.springbatch.repository.CustomerRepository;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Component
public class CustomerWriter implements ItemWriter<Customer> {

  private final CustomerRepository customerRepository;

  public CustomerWriter(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public void write(List<? extends Customer> list) throws Exception {
    System.out.println("Thread Name : -"+Thread.currentThread().getName());
    customerRepository.saveAll(list);
  }
}
