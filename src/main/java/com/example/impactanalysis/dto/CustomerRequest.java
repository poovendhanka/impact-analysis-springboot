package com.example.impactanalysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for customer registration requests.
 * Directly corresponds to the React CustomerRequest TypeScript interface.
 */
@Schema(description = "Customer Registration Request Payload")
public class CustomerRequest {

    @Schema(description = "Customer's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Customer Name is required")
    @Size(min = 2, max = 100, message = "Customer Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Customer's primary email address", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email Address is required")
    @Email(message = "Email Address must be a valid email format")
    private String email;

    @Schema(description = "Customer's 10-digit phone number", example = "9876543210", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone Number must be a valid 10-digit number")
    private String phone;

    @Schema(description = "Optional message or notes from customer", example = "Sample customer registration")
    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;

    @Schema(description = "Optional company or organization name", example = "Acme Inc")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    public CustomerRequest() {
    }

    public CustomerRequest(String name, String email, String phone, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.message = message;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
