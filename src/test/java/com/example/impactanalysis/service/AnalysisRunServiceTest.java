package com.example.impactanalysis.service;

import com.example.impactanalysis.dto.AnalysisRunRequest;
import com.example.impactanalysis.dto.AnalysisRunResponse;
import com.example.impactanalysis.dto.DashboardMetricsResponse;
import com.example.impactanalysis.entity.AnalysisRunEntity;
import com.example.impactanalysis.repository.AnalysisRunRepository;
import com.example.impactanalysis.service.impl.AnalysisRunServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisRunServiceTest {

    @Mock
    private AnalysisRunRepository repository;

    private AnalysisRunService analysisRunService;

    @BeforeEach
    void setUp() {
        analysisRunService = new AnalysisRunServiceImpl(repository);
    }

    @Test
    void testRecordRun_success() {
        AnalysisRunRequest request = new AnalysisRunRequest(
                "run-123",
                "poovendhanka/impact-analysis-springboot",
                "a1b2c3d4",
                42,
                "feature/user-api",
                "HIGH",
                "PASS_WITH_WARNING",
                1.45,
                25,
                30,
                1,
                "# Impact Markdown",
                "{\"severity\": \"HIGH\"}"
        );

        AnalysisRunEntity savedEntity = new AnalysisRunEntity(
                request.runId(),
                request.repository(),
                request.commitSha(),
                request.prNumber(),
                request.branch(),
                request.severity(),
                request.gateDecision(),
                request.durationSeconds(),
                request.totalNodes(),
                request.totalEdges(),
                request.breakingChangesCount(),
                request.reportMarkdown(),
                request.reportJson()
        );
        savedEntity.setId(1L);
        savedEntity.setCreatedAt(LocalDateTime.now());

        when(repository.save(any(AnalysisRunEntity.class))).thenReturn(savedEntity);

        AnalysisRunResponse response = analysisRunService.recordRun(request);

        assertThat(response).isNotNull();
        assertThat(response.runId()).isEqualTo("run-123");
        assertThat(response.severity()).isEqualTo("HIGH");
        assertThat(response.gateDecision()).isEqualTo("PASS_WITH_WARNING");
        verify(repository, times(1)).save(any(AnalysisRunEntity.class));
    }

    @Test
    void testGetRunByRunId_found() {
        AnalysisRunEntity entity = new AnalysisRunEntity(
                "run-999", "repo-x", "sha123", 10, "main", "LOW", "PASS", 0.5, 10, 8, 0, "md", "{}"
        );
        entity.setId(5L);

        when(repository.findByRunId("run-999")).thenReturn(Optional.of(entity));

        Optional<AnalysisRunResponse> result = analysisRunService.getRunByRunId("run-999");

        assertThat(result).isPresent();
        assertThat(result.get().runId()).isEqualTo("run-999");
        assertThat(result.get().severity()).isEqualTo("LOW");
    }

    @Test
    void testGetDashboardMetrics() {
        when(repository.count()).thenReturn(10L);
        when(repository.countBySeverity("CRITICAL")).thenReturn(1L);
        when(repository.countBySeverity("HIGH")).thenReturn(3L);
        when(repository.countBySeverity("MEDIUM")).thenReturn(4L);
        when(repository.countBySeverity("LOW")).thenReturn(2L);
        when(repository.countByGateDecision("PASS")).thenReturn(7L);
        when(repository.countByGateDecision("PASS_WITH_WARNING")).thenReturn(2L);
        when(repository.countByGateDecision("BLOCK")).thenReturn(1L);
        when(repository.countByGateDecision("WOULD_BLOCK")).thenReturn(0L);

        DashboardMetricsResponse metrics = analysisRunService.getDashboardMetrics();

        assertThat(metrics).isNotNull();
        assertThat(metrics.totalAnalysisRuns()).isEqualTo(10L);
        assertThat(metrics.gatePassRate()).isEqualTo(90.0);
        assertThat(metrics.severityBreakdown().get("CRITICAL")).isEqualTo(1L);
        assertThat(metrics.gateDecisionBreakdown().get("PASS")).isEqualTo(7L);
    }
}
