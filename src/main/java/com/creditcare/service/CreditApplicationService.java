
package com.creditcare.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.creditcare.entity.CreditApplication;
import com.creditcare.entity.Customer;
import com.creditcare.repository.CreditApplicationRepository;
import com.creditcare.repository.CustomerRepository;

@Service
public class CreditApplicationService {

    private final CreditApplicationRepository creditApplicationRepository;
    private final CustomerRepository customerRepository;

    public CreditApplicationService(
            CreditApplicationRepository creditApplicationRepository,
            CustomerRepository customerRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.customerRepository = customerRepository;
    }

    // Create an application for an existing customer
    public CreditApplication createApplication(
            Long customerId,
            CreditApplication application) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found: " + customerId));

        if (application.getRequestedAmount() == null
                || application.getRequestedAmount().signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Requested amount must be greater than zero");
        }

        if (application.getAnnualIncome() == null
                || application.getAnnualIncome().signum() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Annual income cannot be negative");
        }

        application.setCustomer(customer);
        application.setStatus("PENDING");

        return creditApplicationRepository.save(application);
    }

    // Get every credit application
    public List<CreditApplication> getAllApplications() {
        return creditApplicationRepository.findAll();
    }

    // Get applications for one customer
    public List<CreditApplication> getApplicationsByCustomer(
            Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + customerId);
        }

        return creditApplicationRepository.findByCustomerId(customerId);
    }

    // Update an application's status
    @Transactional
    public CreditApplication updateStatus(Long id, String status) {

        CreditApplication application =
                creditApplicationRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Application not found: " + id));

        application.setStatus(status);

        return creditApplicationRepository.save(application);
    }
}
