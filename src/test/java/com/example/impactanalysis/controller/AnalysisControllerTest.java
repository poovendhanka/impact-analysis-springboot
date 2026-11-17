package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.AnalysisRunRequest;
import com.example.impactanalysis.dto.AnalysisRunResponse;
import com.example.impactanalysis.service.AnalysisRunService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalysisController.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnalysisRunService analysisRunService;

    @Test
    void testGetHistory() throws Exception {
        AnalysisRunResponse run = new AnalysisRunResponse(
                1L, "run-1", "repo-a", "sha1", 5, "main", "MEDIUM", "PASS", 1.2, 20, 15, 0, "md", "{}", LocalDateTime.now()
        );
        when(analysisRunService.getHistory(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(run)));

        mockMvc.perform(get("/api/analysis/history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].runId").value("run-1"))
                .andExpect(jsonPath("$.content[0].severity").value("MEDIUM"));
    }

    @Test
    void testGetRunById_found() throws Exception {
        AnalysisRunResponse run = new AnalysisRunResponse(
                2L, "run-2", "repo-b", "sha2", 12, "feature", "CRITICAL", "BLOCK", 2.0, 40, 35, 3, "md", "{}", LocalDateTime.now()
        );
        when(analysisRunService.getRunByRunId("run-2")).thenReturn(Optional.of(run));

        mockMvc.perform(get("/api/analysis/runs/run-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.runId").value("run-2"))
                .andExpect(jsonPath("$.gateDecision").value("BLOCK"));
    }

    @Test
    void testGetRunReport_markdown() throws Exception {
        when(analysisRunService.getRunReport("run-2", "markdown"))
                .thenReturn(Optional.of("# Impact Report Content"));

        mockMvc.perform(get("/api/analysis/runs/run-2/report")
                        .param("format", "markdown"))
                .andExpect(status().isOk())
                .andExpect(content().string("# Impact Report Content"));
    }

    @Test
    void testRecordRun_created() throws Exception {
        AnalysisRunRequest request = new AnalysisRunRequest(
                "run-new", "repo-c", "sha3", 8, "dev", "LOW", "PASS", 0.8, 12, 10, 0, "md", "{}"
        );
        AnalysisRunResponse response = new AnalysisRunResponse(
                3L, "run-new", "repo-c", "sha3", 8, "dev", "LOW", "PASS", 0.8, 12, 10, 0, "md", "{}", LocalDateTime.now()
        );

        when(analysisRunService.recordRun(any(AnalysisRunRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/analysis/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.runId").value("run-new"))
                .andExpect(jsonPath("$.severity").value("LOW"));
    }
}
