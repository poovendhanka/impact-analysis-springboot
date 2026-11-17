package com.example.impactanalysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for customer registration responses.
 * Directly corresponds to the React CustomerResponse TypeScript interface.
 */
@Schema(description = "Customer Registration Response Payload")
public class CustomerResponse {

    @Schema(description = "Assigned Customer unique identifier", example = "1")
    private Long id;

    @Schema(description = "Customer's full name", example = "John Doe")
    private String name;

    @Schema(description = "Customer's primary email address", example = "john@example.com")
    private String email;

    @Schema(description = "Customer's message or notes", example = "Sample customer registration")
    private String message;

    @Schema(description = "Registration timestamp in ISO-8601 format", example = "2026-11-16T17:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Operation status indicator", example = "SUCCESS")
    private String status;

    public CustomerResponse() {
    }

    public CustomerResponse(Long id, String name, String email, String message, LocalDateTime createdAt, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomerResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}
