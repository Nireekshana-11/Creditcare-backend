package com.creditcare.controller;

import com.creditcare.entity.CreditApplication;
import com.creditcare.service.CreditApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class CreditApplicationController {

    private final CreditApplicationService creditApplicationService;

    public CreditApplicationController(
            CreditApplicationService creditApplicationService) {
        this.creditApplicationService = creditApplicationService;
    }

    // Submit a new credit application
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditApplication createApplication(
            @RequestParam Long customerId,
            @RequestBody CreditApplication application) {

        return creditApplicationService.createApplication(
                customerId, application);
    }

    // View all applications
    @GetMapping
    public List<CreditApplication> getAllApplications() {
        return creditApplicationService.getAllApplications();
    }

    // View applications belonging to one customer
    @GetMapping("/customer/{customerId}")
    public List<CreditApplication> getApplicationsByCustomer(
            @PathVariable Long customerId) {

        return creditApplicationService
                .getApplicationsByCustomer(customerId);
    }

    // Approve or reject an application (admin only via SecurityConfig)
    @PatchMapping("/{id}/status")
    public CreditApplication updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        if (status == null ||
                !(status.equals("APPROVED")
                || status.equals("REJECTED")
                || status.equals("PENDING"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be APPROVED, REJECTED, or PENDING");
        }

        return creditApplicationService.updateStatus(id, status);
    }
}
