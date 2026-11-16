package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.CustomerRequest;
import com.example.impactanalysis.dto.CustomerResponse;
import com.example.impactanalysis.exception.CustomerAlreadyExistsException;
import com.example.impactanalysis.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("POST /api/customers - returns 201 Created on valid customer request")
    void shouldReturn201WhenCustomerPayloadIsValid() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "John Doe",
                "john@example.com",
                "9876543210",
                "Sample customer registration"
        );

        CustomerResponse response = new CustomerResponse(
                1L,
                "John Doe",
                "john@example.com",
                "9876543210",
                "Sample customer registration",
                LocalDateTime.now(),
                "SUCCESS"
        );

        when(customerService.registerCustomer(any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @DisplayName("POST /api/customers - returns 400 Bad Request when name is blank")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "",
                "john@example.com",
                "9876543210",
                "Sample customer registration"
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists());
    }

    @Test
    @DisplayName("POST /api/customers - returns 400 Bad Request when email format is invalid")
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "John Doe",
                "not-an-email",
                "9876543210",
                "Sample customer registration"
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    @DisplayName("POST /api/customers - returns 400 Bad Request when phone number is not 10 digits")
    void shouldReturn400WhenPhoneIsNotTenDigits() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "John Doe",
                "john@example.com",
                "12345",
                "Sample customer registration"
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.phone").exists());
    }

    @Test
    @DisplayName("POST /api/customers - returns 409 Conflict when email already exists")
    void shouldReturn409WhenCustomerAlreadyExists() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "John Doe",
                "john@example.com",
                "9876543210",
                "Sample customer registration"
        );

        when(customerService.registerCustomer(any(CustomerRequest.class)))
                .thenThrow(new CustomerAlreadyExistsException("A customer with email 'john@example.com' is already registered."));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("already registered")));
    }
}
