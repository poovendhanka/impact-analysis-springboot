package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.DashboardMetricsResponse;
import com.example.impactanalysis.service.AnalysisRunService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({DashboardController.class, GraphController.class})
class DashboardAndGraphControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisRunService analysisRunService;

    @Test
    void testGetDashboardMetrics() throws Exception {
        DashboardMetricsResponse metrics = new DashboardMetricsResponse(
                3L, 4L, 4L, 15L, 86.7, 38, 14, 2,
                Map.of("HIGH", 4L, "MEDIUM", 6L, "LOW", 5L),
                Map.of("PASS", 13L, "BLOCK", 2L)
        );
        when(analysisRunService.getDashboardMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/api/dashboard/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRepositories").value(3))
                .andExpect(jsonPath("$.totalServices").value(4))
                .andExpect(jsonPath("$.gatePassRate").value(86.7));
    }

    @Test
    void testGetContracts() throws Exception {
        mockMvc.perform(get("/api/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetGraph() throws Exception {
        mockMvc.perform(get("/api/graph"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNodes").isNumber())
                .andExpect(jsonPath("$.totalEdges").isNumber());
    }

    @Test
    void testSearchNodes() throws Exception {
        mockMvc.perform(get("/api/graph/nodes").param("query", "Customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetDownstreamBlastRadius() throws Exception {
        mockMvc.perform(get("/api/graph/downstream")
                        .param("nodeId", "sb::POST::/api/v1/customers")
                        .param("maxDepth", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNodes").isNumber());
    }
}
