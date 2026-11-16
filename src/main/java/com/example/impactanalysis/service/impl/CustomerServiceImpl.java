package com.example.impactanalysis.service.impl;

import com.example.impactanalysis.dto.CustomerRequest;
import com.example.impactanalysis.dto.CustomerResponse;
import com.example.impactanalysis.entity.CustomerEntity;
import com.example.impactanalysis.exception.CustomerAlreadyExistsException;
import com.example.impactanalysis.repository.CustomerRepository;
import com.example.impactanalysis.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation managing customer registration workflow,
 * data normalization, and database interaction.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }

        // 1. Normalize data: trim whitespace and lowercase email (Phase 6 Scenario 2 internal update)
        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String trimmedName = request.getName() != null ? request.getName().trim() : "";
        String trimmedPhone = request.getPhone() != null ? request.getPhone().trim() : "";
        String trimmedMessage = request.getMessage() != null ? request.getMessage().trim() : "";

        // 2. Check for duplicate registration by email
        if (customerRepository.existsByEmail(normalizedEmail)) {
            throw new CustomerAlreadyExistsException("A customer with email '" + normalizedEmail + "' is already registered.");
        }

        // 3. Map to CustomerEntity
        CustomerEntity customerEntity = new CustomerEntity(
                trimmedName,
                normalizedEmail,
                trimmedPhone,
                trimmedMessage.isEmpty() ? null : trimmedMessage
        );

        // 4. Save to PostgreSQL database
        CustomerEntity savedEntity = customerRepository.save(customerEntity);

        // 5. Build and return structured response
        return mapToResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + id + " not found"));
        return mapToResponse(entity);
    }

    private CustomerResponse mapToResponse(CustomerEntity entity) {
        return new CustomerResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getMessage(),
                entity.getCreatedAt(),
                "SUCCESS"
        );
    }
}
