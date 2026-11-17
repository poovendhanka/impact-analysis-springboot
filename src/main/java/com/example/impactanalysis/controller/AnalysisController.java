package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.AnalysisRunRequest;
import com.example.impactanalysis.dto.AnalysisRunResponse;
import com.example.impactanalysis.service.AnalysisRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@Tag(name = "Analysis Management", description = "Endpoints for managing and querying impact analysis runs and reports")
public class AnalysisController {

    private final AnalysisRunService analysisRunService;

    public AnalysisController(AnalysisRunService analysisRunService) {
        this.analysisRunService = analysisRunService;
    }

    @GetMapping("/history")
    @Operation(summary = "Get historical analysis runs with pagination and multi-facet filtering")
    public ResponseEntity<Page<AnalysisRunResponse>> getHistory(
            @RequestParam(required = false) String repository,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String gateDecision,
            @RequestParam(required = false) Integer prNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort.length > 0 && sort[0].contains(",")) {
            String[] parts = sort[0].split(",");
            sortObj = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
        }
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(analysisRunService.getHistory(repository, severity, gateDecision, prNumber, pageable));
    }

    @GetMapping("/runs/{runId}")
    @Operation(summary = "Get full details of a specific analysis run by run ID")
    public ResponseEntity<AnalysisRunResponse> getRunById(@PathVariable String runId) {
        return analysisRunService.getRunByRunId(runId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/runs/{runId}/report", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_MARKDOWN_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Get formatted analysis report in JSON or Markdown")
    public ResponseEntity<String> getRunReport(
            @PathVariable String runId,
            @RequestParam(defaultValue = "markdown") String format
    ) {
        return analysisRunService.getRunReport(runId, format)
                .map(content -> {
                    MediaType mediaType = "json".equalsIgnoreCase(format) ? MediaType.APPLICATION_JSON : MediaType.TEXT_MARKDOWN;
                    return ResponseEntity.ok().contentType(mediaType).body(content);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/runs")
    @Operation(summary = "Record or persist a new impact analysis run")
    public ResponseEntity<AnalysisRunResponse> recordRun(@Valid @RequestBody AnalysisRunRequest request) {
        AnalysisRunResponse response = analysisRunService.recordRun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
