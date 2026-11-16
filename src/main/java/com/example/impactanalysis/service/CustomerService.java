package com.example.impactanalysis.service;

import com.example.impactanalysis.dto.CustomerRequest;
import com.example.impactanalysis.dto.CustomerResponse;

import java.util.List;

/**
 * Service contract for customer registration and management operations.
 */
public interface CustomerService {

    /**
     * Registers a new customer after input normalization and validation.
     *
     * @param request Validated CustomerRequest payload
     * @return Generated CustomerResponse containing persistence metadata
     */
    CustomerResponse registerCustomer(CustomerRequest request);

    /**
     * Retrieves all registered customers.
     *
     * @return List of CustomerResponse objects
     */
    List<CustomerResponse> getAllCustomers();

    /**
     * Retrieves a customer by unique ID.
     *
     * @param id Customer primary key
     * @return CustomerResponse matching ID
     */
    CustomerResponse getCustomerById(Long id);
}
