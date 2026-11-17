package com.example.impactanalysis.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GovernanceController.class)
class GovernanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/governance/status - returns 200 OK with active policy and mandatory rules")
    void shouldReturnGovernanceStatus() throws Exception {
        mockMvc.perform(get("/api/governance/status").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationName", is("AcmeCorp")))
                .andExpect(jsonPath("$.activeTier", is("SERVICE_OVERRIDE")))
                .andExpect(jsonPath("$.environment", is("production")))
                .andExpect(jsonPath("$.allowOverrides", is(false)))
                .andExpect(jsonPath("$.blockOnSeverity", hasItems("CRITICAL", "HIGH")))
                .andExpect(jsonPath("$.mandatoryRulesEnforced", hasItems("ORG-MANDATE-001", "ORG-MANDATE-002")));
    }

    @Test
    @DisplayName("GET /api/governance/integrations - returns 200 OK with health and masked tokens")
    void shouldReturnIntegrationsHealthWithMaskedSecrets() throws Exception {
        mockMvc.perform(get("/api/governance/integrations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].providerName", is("github-enterprise")))
                .andExpect(jsonPath("$[0].status", is("HEALTHY")))
                .andExpect(jsonPath("$[0].maskedToken", containsString("***")))
                .andExpect(jsonPath("$[0].critical", is(true)))
                // Verify no raw unmasked tokens exposed
                .andExpect(jsonPath("$[*].maskedToken", everyItem(not(matchesRegex("^[a-zA-Z0-9_-]{20,}$")))));
    }

    @Test
    @DisplayName("GET /api/governance/audit - returns 200 OK with cryptographic hash chain")
    void shouldReturnCryptographicAuditEvents() throws Exception {
        mockMvc.perform(get("/api/governance/audit").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].sequenceId", is(1)))
                .andExpect(jsonPath("$[0].eventType", is("ANALYSIS_RUN")))
                .andExpect(jsonPath("$[0].tamperVerified", is(true)))
                .andExpect(jsonPath("$[0].recordHash", hasLength(64)));
    }
}
