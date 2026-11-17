package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.ContractItemDto;
import com.example.impactanalysis.dto.DashboardMetricsResponse;
import com.example.impactanalysis.service.AnalysisRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Dashboard & Catalog", description = "High-level metrics and catalog contracts for the developer dashboard")
public class DashboardController {

    private final AnalysisRunService analysisRunService;

    public DashboardController(AnalysisRunService analysisRunService) {
        this.analysisRunService = analysisRunService;
    }

    @GetMapping("/dashboard/metrics")
    @Operation(summary = "Get aggregated summary metrics for the impact analysis developer dashboard")
    public ResponseEntity<DashboardMetricsResponse> getMetrics() {
        return ResponseEntity.ok(analysisRunService.getDashboardMetrics());
    }

    @GetMapping("/contracts")
    @Operation(summary = "List all discovered OpenAPI and AsyncAPI contracts across repositories")
    public ResponseEntity<List<ContractItemDto>> getContracts() {
        List<ContractItemDto> contracts = List.of(
                new ContractItemDto(
                        "openapi-customer-v1",
                        "Customer Service REST API",
                        "OPENAPI",
                        "impact-analysis-springboot",
                        "1.0.0",
                        "openapi.yaml",
                        4
                ),
                new ContractItemDto(
                        "asyncapi-customer-events-v2",
                        "Customer Lifecycle Events",
                        "ASYNCAPI",
                        "impact-analysis-springboot",
                        "2.0.0",
                        "asyncapi.yaml",
                        2
                ),
                new ContractItemDto(
                        "openapi-orders-v1",
                        "Order Management API",
                        "OPENAPI",
                        "impact-analysis-engine",
                        "1.0.0",
                        "fixtures/contracts/openapi_customer_v1.yaml",
                        3
                ),
                new ContractItemDto(
                        "asyncapi-notifications-v3",
                        "Notification Streaming Events",
                        "ASYNCAPI",
                        "impact-analysis-engine",
                        "3.0.0",
                        "fixtures/asyncapi/asyncapi_notifications_v3.yaml",
                        2
                )
        );
        return ResponseEntity.ok(contracts);
    }
}
