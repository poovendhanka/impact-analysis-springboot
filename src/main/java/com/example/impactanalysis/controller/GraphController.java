package com.example.impactanalysis.controller;

import com.example.impactanalysis.dto.GraphQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/graph")
@Tag(name = "Dependency Graph", description = "Query, search, and navigate CIR dependency relationships")
public class GraphController {

    private final List<Map<String, Object>> defaultNodes = new ArrayList<>();
    private final List<Map<String, Object>> defaultEdges = new ArrayList<>();

    public GraphController() {
        initializeSampleGraph();
    }

    private void initializeSampleGraph() {
        // Backend Spring Boot Nodes
        addNode("sb::CustomerController", "CustomerController", "CLASS", "springboot", "java", "CustomerController.java", 23);
        addNode("sb::POST::/api/v1/customers", "POST /api/v1/customers", "ENDPOINT", "springboot", "java", "CustomerController.java", 35);
        addNode("sb::GET::/api/v1/customers", "GET /api/v1/customers", "ENDPOINT", "springboot", "java", "CustomerController.java", 48);
        addNode("sb::CustomerService", "CustomerService", "INTERFACE", "springboot", "java", "CustomerService.java", 12);
        addNode("sb::CustomerServiceImpl", "CustomerServiceImpl", "CLASS", "springboot", "java", "CustomerServiceImpl.java", 18);
        addNode("sb::CustomerRepository", "CustomerRepository", "INTERFACE", "springboot", "java", "CustomerRepository.java", 10);
        addNode("sb::CustomerEntity", "CustomerEntity", "CLASS", "springboot", "java", "CustomerEntity.java", 15);
        addNode("sb::CustomerRequest", "CustomerRequest", "DTO", "springboot", "java", "CustomerRequest.java", 8);

        // Contract Nodes
        addNode("contract::openapi::customers", "OpenAPI: Customer Service API v1", "CONTRACT", "springboot", "yaml", "openapi.yaml", 1);
        addNode("contract::asyncapi::events", "AsyncAPI: Customer Events v2", "CONTRACT", "springboot", "yaml", "asyncapi.yaml", 1);
        addNode("event::customer.registered", "customer.registered", "CHANNEL", "springboot", "yaml", "asyncapi.yaml", 14);

        // Frontend React Nodes
        addNode("react::CustomerRegistrationForm", "CustomerRegistrationForm", "COMPONENT", "react", "typescript", "CustomerRegistrationForm.tsx", 15);
        addNode("react::customerService", "customerService.ts", "MODULE", "react", "typescript", "customerService.ts", 1);
        addNode("react::App", "App", "COMPONENT", "react", "typescript", "App.tsx", 10);

        // Edges (Calls, Implements, Uses, Consumes, Publishes)
        addEdge("sb::CustomerController", "sb::POST::/api/v1/customers", "EXPOSES_API", "DECLARED", 1.0, "Spring @PostMapping");
        addEdge("sb::CustomerController", "sb::GET::/api/v1/customers", "EXPOSES_API", "DECLARED", 1.0, "Spring @GetMapping");
        addEdge("sb::CustomerController", "sb::CustomerService", "CALLS", "DECLARED", 1.0, "Injected bean call");
        addEdge("sb::CustomerServiceImpl", "sb::CustomerService", "IMPLEMENTS", "DECLARED", 1.0, "Class implements interface");
        addEdge("sb::CustomerServiceImpl", "sb::CustomerRepository", "CALLS", "DECLARED", 1.0, "JPA repository invocation");
        addEdge("sb::CustomerRepository", "sb::CustomerEntity", "MANAGES", "DECLARED", 1.0, "Generic entity mapping");
        addEdge("sb::CustomerController", "sb::CustomerRequest", "USES_SCHEMA", "DECLARED", 1.0, "RequestBody parameter");

        // Contract Edges
        addEdge("sb::POST::/api/v1/customers", "contract::openapi::customers", "DEFINED_BY_CONTRACT", "INFERRED", 0.95, "OpenAPI operationId: createCustomer");
        addEdge("sb::CustomerServiceImpl", "event::customer.registered", "PUBLISHES_EVENT", "DECLARED", 1.0, "Async event publisher");
        addEdge("event::customer.registered", "contract::asyncapi::events", "DEFINED_BY_CONTRACT", "INFERRED", 0.95, "AsyncAPI channel definition");

        // Cross-Repository Edges (React -> Spring Boot)
        addEdge("react::CustomerRegistrationForm", "react::customerService", "USES", "DECLARED", 1.0, "Import and call");
        addEdge("react::customerService", "sb::POST::/api/v1/customers", "CALLS_HTTP", "OBSERVED", 0.98, "OTLP trace span: POST /api/v1/customers");
        addEdge("react::App", "react::CustomerRegistrationForm", "RENDERS", "DECLARED", 1.0, "JSX component nesting");
    }

    private void addNode(String id, String name, String type, String repository, String language, String filePath, int lineStart) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("id", id);
        node.put("name", name);
        node.put("type", type);
        node.put("repository", repository);
        node.put("language", language);
        node.put("filePath", filePath);
        node.put("lineStart", lineStart);
        defaultNodes.add(node);
    }

    private void addEdge(String source, String target, String type, String nature, double confidence, String evidence) {
        Map<String, Object> edge = new LinkedHashMap<>();
        edge.put("source", source);
        edge.put("target", target);
        edge.put("type", type);
        edge.put("nature", nature);
        edge.put("confidence", confidence);
        edge.put("evidence", evidence);
        edge.put("isCrossRepository", !source.split("::")[0].equals(target.split("::")[0]));
        defaultEdges.add(edge);
    }

    @GetMapping
    @Operation(summary = "Get complete or filtered CIR dependency graph")
    public ResponseEntity<GraphQueryResponse> getGraph(
            @RequestParam(required = false) String repository,
            @RequestParam(required = false) String type
    ) {
        List<Map<String, Object>> filteredNodes = defaultNodes.stream()
                .filter(n -> repository == null || repository.equalsIgnoreCase((String) n.get("repository")))
                .filter(n -> type == null || type.equalsIgnoreCase((String) n.get("type")))
                .collect(Collectors.toList());

        Set<String> nodeIds = filteredNodes.stream().map(n -> (String) n.get("id")).collect(Collectors.toSet());
        List<Map<String, Object>> filteredEdges = defaultEdges.stream()
                .filter(e -> nodeIds.contains((String) e.get("source")) && nodeIds.contains((String) e.get("target")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GraphQueryResponse(filteredNodes.size(), filteredEdges.size(), filteredNodes, filteredEdges));
    }

    @GetMapping("/nodes")
    @Operation(summary = "Search nodes by keyword, type, repository, or language")
    public ResponseEntity<List<Map<String, Object>>> searchNodes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String repository,
            @RequestParam(required = false) String language
    ) {
        List<Map<String, Object>> matches = defaultNodes.stream()
                .filter(n -> query == null || ((String) n.get("name")).toLowerCase().contains(query.toLowerCase()) || ((String) n.get("id")).toLowerCase().contains(query.toLowerCase()))
                .filter(n -> type == null || type.equalsIgnoreCase((String) n.get("type")))
                .filter(n -> repository == null || repository.equalsIgnoreCase((String) n.get("repository")))
                .filter(n -> language == null || language.equalsIgnoreCase((String) n.get("language")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/upstream")
    @Operation(summary = "Query upstream callers and dependencies for a target node")
    public ResponseEntity<GraphQueryResponse> getUpstream(
            @RequestParam String nodeId,
            @RequestParam(defaultValue = "2") int maxDepth
    ) {
        Set<String> visitedNodes = new LinkedHashSet<>();
        Set<Map<String, Object>> collectedEdges = new LinkedHashSet<>();
        collectUpstream(nodeId, maxDepth, visitedNodes, collectedEdges);
        visitedNodes.add(nodeId);

        List<Map<String, Object>> nodes = defaultNodes.stream()
                .filter(n -> visitedNodes.contains((String) n.get("id")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GraphQueryResponse(nodes.size(), collectedEdges.size(), nodes, new ArrayList<>(collectedEdges)));
    }

    @GetMapping("/downstream")
    @Operation(summary = "Query downstream blast radius and impacted targets for a changed node")
    public ResponseEntity<GraphQueryResponse> getDownstream(
            @RequestParam String nodeId,
            @RequestParam(defaultValue = "3") int maxDepth
    ) {
        Set<String> visitedNodes = new LinkedHashSet<>();
        Set<Map<String, Object>> collectedEdges = new LinkedHashSet<>();
        collectDownstream(nodeId, maxDepth, visitedNodes, collectedEdges);
        visitedNodes.add(nodeId);

        List<Map<String, Object>> nodes = defaultNodes.stream()
                .filter(n -> visitedNodes.contains((String) n.get("id")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GraphQueryResponse(nodes.size(), collectedEdges.size(), nodes, new ArrayList<>(collectedEdges)));
    }

    private void collectUpstream(String currentId, int depth, Set<String> visited, Set<Map<String, Object>> edges) {
        if (depth <= 0) return;
        for (Map<String, Object> edge : defaultEdges) {
            if (currentId.equals(edge.get("target"))) {
                String source = (String) edge.get("source");
                edges.add(edge);
                if (visited.add(source)) {
                    collectUpstream(source, depth - 1, visited, edges);
                }
            }
        }
    }

    private void collectDownstream(String currentId, int depth, Set<String> visited, Set<Map<String, Object>> edges) {
        if (depth <= 0) return;
        for (Map<String, Object> edge : defaultEdges) {
            if (currentId.equals(edge.get("source"))) {
                String target = (String) edge.get("target");
                edges.add(edge);
                if (visited.add(target)) {
                    collectDownstream(target, depth - 1, visited, edges);
                }
            }
        }
    }
}
