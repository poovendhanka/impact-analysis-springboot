package com.example.impactanalysis.repository;

import com.example.impactanalysis.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for CustomerEntity persistence operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    /**
     * Checks if a customer with the given normalized email already exists.
     * @param email Normalized email string
     * @return true if record exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a customer by email.
     * @param email Normalized email string
     * @return Optional containing CustomerEntity if present
     */
    Optional<CustomerEntity> findByEmail(String email);
}
