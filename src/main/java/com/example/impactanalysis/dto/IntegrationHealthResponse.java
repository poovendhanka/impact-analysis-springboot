package com.example.impactanalysis.dto;

/**
 * Enterprise Integration Provider Health DTO.
 * Exposes health checks and safe masked credentials.
 */
public class IntegrationHealthResponse {
    private String providerName;
    private String providerType;
    private String status;
    private double latencyMs;
    private String message;
    private String maskedToken;
    private boolean critical;

    public IntegrationHealthResponse() {
    }

    public IntegrationHealthResponse(
            String providerName,
            String providerType,
            String status,
            double latencyMs,
            String message,
            String maskedToken,
            boolean critical
    ) {
        this.providerName = providerName;
        this.providerType = providerType;
        this.status = status;
        this.latencyMs = latencyMs;
        this.message = message;
        this.maskedToken = maskedToken;
        this.critical = critical;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(double latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaskedToken() {
        return maskedToken;
    }

    public void setMaskedToken(String maskedToken) {
        this.maskedToken = maskedToken;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }
}
