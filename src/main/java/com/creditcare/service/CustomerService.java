package com.creditcare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.creditcare.entity.Customer;
import com.creditcare.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer registerCustomer(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}
