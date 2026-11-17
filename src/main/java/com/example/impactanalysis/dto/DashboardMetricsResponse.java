package com.example.impactanalysis.dto;

import java.util.Map;

public record DashboardMetricsResponse(
    long totalRepositories,
    long totalServices,
    long totalContracts,
    long totalAnalysisRuns,
    double gatePassRate,
    int declaredEdgesCount,
    int observedEdgesCount,
    int staticRuntimeDiscrepancies,
    Map<String, Long> severityBreakdown,
    Map<String, Long> gateDecisionBreakdown
) {}
