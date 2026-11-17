package com.example.impactanalysis.dto;

import java.util.List;
import java.util.Map;

public record GraphQueryResponse(
    int totalNodes,
    int totalEdges,
    List<Map<String, Object>> nodes,
    List<Map<String, Object>> edges
) {}
