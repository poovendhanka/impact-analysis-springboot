package com.example.impactanalysis.dto;

/**
 * Enterprise Cryptographic Audit Trail Event DTO.
 * Exposes hash-chained audit events with tamper-evident verification.
 */
public class AuditEventResponse {
    private int sequenceId;
    private String eventId;
    private String eventType;
    private String actor;
    private String repository;
    private String timestamp;
    private String recordHash;
    private boolean tamperVerified;

    public AuditEventResponse() {
    }

    public AuditEventResponse(
            int sequenceId,
            String eventId,
            String eventType,
            String actor,
            String repository,
            String timestamp,
            String recordHash,
            boolean tamperVerified
    ) {
        this.sequenceId = sequenceId;
        this.eventId = eventId;
        this.eventType = eventType;
        this.actor = actor;
        this.repository = repository;
        this.timestamp = timestamp;
        this.recordHash = recordHash;
        this.tamperVerified = tamperVerified;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecordHash() {
        return recordHash;
    }

    public void setRecordHash(String recordHash) {
        this.recordHash = recordHash;
    }

    public boolean isTamperVerified() {
        return tamperVerified;
    }

    public void setTamperVerified(boolean tamperVerified) {
        this.tamperVerified = tamperVerified;
    }
}
