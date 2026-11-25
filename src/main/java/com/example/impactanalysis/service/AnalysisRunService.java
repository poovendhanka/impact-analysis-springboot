package com.example.impactanalysis.service;

import com.example.impactanalysis.dto.AnalysisRunRequest;
import com.example.impactanalysis.dto.AnalysisRunResponse;
import com.example.impactanalysis.dto.DashboardMetricsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnalysisRunService {

    AnalysisRunResponse recordRun(AnalysisRunRequest request);

    Page<AnalysisRunResponse> getHistory(String repository, String severity, String gateDecision, Integer prNumber, Pageable pageable);
//This cmnt should not affect our PR lets see
    //Lets see how this Comment is handled
    Optional<AnalysisRunResponse> getRunByRunId(String runId);

    Optional<String> getRunReport(String runId, String format);

    DashboardMetricsResponse getDashboardMetrics();
}
