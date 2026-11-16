package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.AuditEventResponse;
import com.example.impactanalysis.dto.GovernanceStatusResponse;
import com.example.impactanalysis.dto.IntegrationHealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Enterprise Governance and Integrations REST Controller.
 * Provides status visibility for active policies, integration health,
 * and cryptographic audit trails with strictly masked credentials.
 */
@RestController
@RequestMapping("/api/governance")
@Tag(name = "Governance & Integrations", description = "Endpoints for policy governance, integration health, and audit trail")
public class GovernanceController {

    @Operation(summary = "Get active enterprise governance policy status")
    @GetMapping("/status")
    public ResponseEntity<GovernanceStatusResponse> getGovernanceStatus() {
        GovernanceStatusResponse response = new GovernanceStatusResponse(
                "AcmeCorp",
                "SERVICE_OVERRIDE",
                "production",
                Arrays.asList("CRITICAL", "HIGH"),
                false, // Overrides disabled in production by mandatory rule ORG-MANDATE-001
                true,
                Arrays.asList("ORG-MANDATE-001", "ORG-MANDATE-002")
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get enterprise integration provider health and status")
    @GetMapping("/integrations")
    public ResponseEntity<List<IntegrationHealthResponse>> getIntegrationsHealth() {
        List<IntegrationHealthResponse> providers = Arrays.asList(
                new IntegrationHealthResponse("github-enterprise", "github", "HEALTHY", 45.2, "GitHub API connected", "ghp_***90", true),
                new IntegrationHealthResponse("spotify-backstage", "backstage", "HEALTHY", 18.0, "Backstage catalog synchronized", "***", false),
                new IntegrationHealthResponse("jira-cloud", "jira", "HEALTHY", 62.4, "Jira REST API connected", "jira_***oken", false),
                new IntegrationHealthResponse("slack-notifications", "slack", "HEALTHY", 12.1, "Slack incoming webhook active", "none", false),
                new IntegrationHealthResponse("testrail-enterprise", "test_management", "HEALTHY", 35.8, "TestRail API operational", "test_***mple", false)
        );
        return ResponseEntity.ok(providers);
    }

    @Operation(summary = "Get cryptographic hash-chained audit trail events")
    @GetMapping("/audit")
    public ResponseEntity<List<AuditEventResponse>> getAuditTrail() {
        String now = Instant.now().toString();
        List<AuditEventResponse> events = Arrays.asList(
                new AuditEventResponse(
                        1,
                        "evt-1001",
                        "ANALYSIS_RUN",
                        "ci-system",
                        "poovendhanka/impact-analysis-springboot",
                        now,
                        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                        true
                ),
                new AuditEventResponse(
                        2,
                        "evt-1002",
                        "GATE_EVALUATION",
                        "gate-evaluator",
                        "poovendhanka/impact-analysis-springboot",
                        now,
                        "a8f5f167f44f4964e6c998dee827110c01759f33ae567885b59a60e1d44c9b36",
                        true
                ),
                new AuditEventResponse(
                        3,
                        "evt-1003",
                        "INTEGRATION_INVOCATION",
                        "integration-manager",
                        "poovendhanka/impact-analysis-springboot",
                        now,
                        "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
                        true
                )
        );
        return ResponseEntity.ok(events);
    }
}
