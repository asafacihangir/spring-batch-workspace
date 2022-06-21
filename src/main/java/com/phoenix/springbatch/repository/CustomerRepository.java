package com.phoenix.springbatch.repository;

import com.phoenix.springbatch.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
}