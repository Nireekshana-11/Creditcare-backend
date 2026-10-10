package com.creditcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creditcare.entity.CreditApplication;

public interface CreditApplicationRepository
        extends JpaRepository<CreditApplication, Long> {

    List<CreditApplication> findByCustomerId(Long customerId);
}
