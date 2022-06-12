package com.endava.tmd.customer.adapter.out.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endava.tmd.customer.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
