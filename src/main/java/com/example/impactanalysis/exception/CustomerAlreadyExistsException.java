package com.example.impactanalysis.exception;

/**
 * Exception thrown when attempting to register a customer whose email already exists.
 */
public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
