package com.example.impactanalysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnalysisRunRequest(
    String runId,
    @NotBlank(message = "Repository is required")
    String repository,
    @NotBlank(message = "Commit SHA is required")
    String commitSha,
    Integer prNumber,
    String branch,
    @NotBlank(message = "Severity is required")
    String severity,
    @NotBlank(message = "Gate decision is required")
    String gateDecision,
    Double durationSeconds,
    Integer totalNodes,
    Integer totalEdges,
    Integer breakingChangesCount,
    String reportMarkdown,
    String reportJson
) {}
