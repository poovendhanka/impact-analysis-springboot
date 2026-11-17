package com.example.impactanalysis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_runs", indexes = {
    @Index(name = "idx_analysis_runs_repo", columnList = "repository"),
    @Index(name = "idx_analysis_runs_severity", columnList = "severity"),
    @Index(name = "idx_analysis_runs_gate", columnList = "gateDecision"),
    @Index(name = "idx_analysis_runs_created", columnList = "createdAt")
})
public class AnalysisRunEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_id", nullable = false, unique = true, length = 64)
    private String runId;

    @Column(nullable = false, length = 255)
    private String repository;

    @Column(name = "commit_sha", nullable = false, length = 64)
    private String commitSha;

    @Column(name = "pr_number")
    private Integer prNumber;

    @Column(length = 128)
    private String branch;

    @Column(nullable = false, length = 32)
    private String severity;

    @Column(name = "gate_decision", nullable = false, length = 32)
    private String gateDecision;

    @Column(name = "duration_seconds")
    private Double durationSeconds = 0.0;

    @Column(name = "total_nodes")
    private Integer totalNodes = 0;

    @Column(name = "total_edges")
    private Integer totalEdges = 0;

    @Column(name = "breaking_changes_count")
    private Integer breakingChangesCount = 0;

    @Column(name = "report_markdown", columnDefinition = "TEXT")
    private String reportMarkdown;

    @Column(name = "report_json", columnDefinition = "TEXT")
    private String reportJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public AnalysisRunEntity() {
    }

    public AnalysisRunEntity(String runId, String repository, String commitSha, Integer prNumber,
                             String branch, String severity, String gateDecision, Double durationSeconds,
                             Integer totalNodes, Integer totalEdges, Integer breakingChangesCount,
                             String reportMarkdown, String reportJson) {
        this.runId = runId;
        this.repository = repository;
        this.commitSha = commitSha;
        this.prNumber = prNumber;
        this.branch = branch;
        this.severity = severity;
        this.gateDecision = gateDecision;
        this.durationSeconds = durationSeconds;
        this.totalNodes = totalNodes;
        this.totalEdges = totalEdges;
        this.breakingChangesCount = breakingChangesCount;
        this.reportMarkdown = reportMarkdown;
        this.reportJson = reportJson;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public Integer getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(Integer prNumber) {
        this.prNumber = prNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getGateDecision() {
        return gateDecision;
    }

    public void setGateDecision(String gateDecision) {
        this.gateDecision = gateDecision;
    }

    public Double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(Integer totalNodes) {
        this.totalNodes = totalNodes;
    }

    public Integer getTotalEdges() {
        return totalEdges;
    }

    public void setTotalEdges(Integer totalEdges) {
        this.totalEdges = totalEdges;
    }

    public Integer getBreakingChangesCount() {
        return breakingChangesCount;
    }

    public void setBreakingChangesCount(Integer breakingChangesCount) {
        this.breakingChangesCount = breakingChangesCount;
    }

    public String getReportMarkdown() {
        return reportMarkdown;
    }

    public void setReportMarkdown(String reportMarkdown) {
        this.reportMarkdown = reportMarkdown;
    }

    public String getReportJson() {
        return reportJson;
    }

    public void setReportJson(String reportJson) {
        this.reportJson = reportJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
