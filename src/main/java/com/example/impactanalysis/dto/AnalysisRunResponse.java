package com.example.impactanalysis.dto;

import java.time.LocalDateTime;

public record AnalysisRunResponse(
    Long id,
    String runId,
    String repository,
    String commitSha,
    Integer prNumber,
    String branch,
    String severity,
    String gateDecision,
    Double durationSeconds,
    Integer totalNodes,
    Integer totalEdges,
    Integer breakingChangesCount,
    String reportMarkdown,
    String reportJson,
    LocalDateTime createdAt
) {}
