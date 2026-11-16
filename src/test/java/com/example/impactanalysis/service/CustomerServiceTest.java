package com.example.impactanalysis.service;

import com.example.impactanalysis.dto.CustomerRequest;
import com.example.impactanalysis.dto.CustomerResponse;
import com.example.impactanalysis.entity.CustomerEntity;
import com.example.impactanalysis.exception.CustomerAlreadyExistsException;
import com.example.impactanalysis.repository.CustomerRepository;
import com.example.impactanalysis.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    @DisplayName("Should successfully register customer with normalized email and trimmed fields")
    void shouldRegisterCustomerSuccessfully() {
        // Arrange
        CustomerRequest request = new CustomerRequest(
                "  John Doe  ",
                "  JOHN.DOE@EXAMPLE.COM  ",
                "  9876543210  ",
                "  Sample customer registration notes  "
        );

        when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(false);

        CustomerEntity savedMockEntity = new CustomerEntity(
                "John Doe",
                "john.doe@example.com",
                "9876543210",
                "Sample customer registration notes"
        );
        savedMockEntity.setId(100L);
        savedMockEntity.setCreatedAt(LocalDateTime.now());

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedMockEntity);

        // Act
        CustomerResponse response = customerService.registerCustomer(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getPhone()).isEqualTo("9876543210");
        assertThat(response.getMessage()).isEqualTo("Sample customer registration notes");
        assertThat(response.getStatus()).isEqualTo("SUCCESS");

        ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepository).save(entityCaptor.capture());
        CustomerEntity captured = entityCaptor.getValue();
        assertThat(captured.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(captured.getName()).isEqualTo("John Doe");
        assertThat(captured.getPhone()).isEqualTo("9876543210");
    }

    @Test
    @DisplayName("Should throw CustomerAlreadyExistsException when email is already registered")
    void shouldThrowExceptionWhenCustomerEmailAlreadyExists() {
        // Arrange
        CustomerRequest request = new CustomerRequest(
                "Jane Doe",
                "jane@example.com",
                "9876543210",
                "Duplicate registration test"
        );

        when(customerRepository.existsByEmail("jane@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> customerService.registerCustomer(request))
                .isInstanceOf(CustomerAlreadyExistsException.class)
                .hasMessageContaining("jane@example.com");

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request payload is null")
    void shouldThrowExceptionWhenRequestIsNull() {
        assertThatThrownBy(() -> customerService.registerCustomer(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
