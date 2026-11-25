# API

The application exposes REST APIs under `/api`. No authentication or authorization layer is implemented in the controllers or Spring configuration on Stage.

## Customer Management

Base path: `/api/customers`

### Create a customer

`POST /api/customers`

Request body (`CustomerRequest`):

| Field | Type | Required | Constraints |
|---|---|---:|---|
| `name` | string | yes | 2-100 characters after trimming |
| `email` | string | yes | Valid email; normalized to lowercase |
| `phone` | string | yes | Exactly 10 digits |
| `message` | string | no | Maximum 500 characters |

Responses:

- `201 Created` — `CustomerResponse`
- `400 Bad Request` — validation or illegal request details
- `409 Conflict` — email already exists

`CustomerResponse` contains `id`, `name`, `email`, `phone`, `message`, `createdAt`, and `status`.

### List customers

`GET /api/customers`

Returns `200 OK` with an array of `CustomerResponse`.

### Get customer

`GET /api/customers/{id}`

Returns `200 OK` with `CustomerResponse`, or `404 Not Found` when the ID does not exist.

Implementation: `CustomerController` -> `CustomerServiceImpl` -> `CustomerRepository` -> JPA.

## Analysis Runs

Base path: `/api/analysis`

### Record or update an analysis run

`POST /api/analysis/runs`

Request body (`AnalysisRunRequest`):

| Field | Type | Required |
|---|---|---:|
| `runId` | string | no; generated UUID when blank or absent |
| `repository` | string | yes |
| `commitSha` | string | yes |
| `prNumber` | integer | no |
| `branch` | string | no; stored as `main` when absent |
| `severity` | string | yes; stored uppercase |
| `gateDecision` | string | yes; stored uppercase |
| `durationSeconds` | number | no; defaults to `0.0` |
| `totalNodes` | integer | no; defaults to `0` |
| `totalEdges` | integer | no; defaults to `0` |
| `breakingChangesCount` | integer | no; defaults to `0` |
| `reportMarkdown` | string | no; defaults to empty |
| `reportJson` | string | no; defaults to empty |

Returns `201 Created` with `AnalysisRunResponse`.

An existing run is updated when the same `runId` is submitted.

### Get analysis history

`GET /api/analysis/history`

Query parameters:

| Parameter | Default | Description |
|---|---|---|
| `repository` | none | Case-insensitive substring match |
| `severity` | none | Exact severity, matched case-insensitively |
| `gateDecision` | none | Exact gate decision, matched case-insensitively |
| `prNumber` | none | Exact pull-request number |
| `page` | `0` | Zero-based page index |
| `size` | `10` | Page size |
| `sort` | `createdAt,desc` | Used sorting field and direction when in `field,direction` form |

Returns `200 OK` with a Spring `Page<AnalysisRunResponse>`.

### Get one analysis run

`GET /api/analysis/runs/{runId}`

Returns `200 OK` with `AnalysisRunResponse`, or `404 Not Found`.

### Get an analysis report

`GET /api/analysis/runs/{runId}/report?format=markdown`

`format=json` returns the stored `reportJson` as `application/json`; other values return `reportMarkdown` as `text/markdown`.

Returns `404 Not Found` when the run ID does not exist.

## Dashboard and Contracts

### Dashboard metrics

`GET /api/dashboard/metrics`

Returns `DashboardMetricsResponse` with repository/service/contract/run totals, gate pass rate, edge counts, static/runtime discrepancy count, severity breakdown, and gate-decision breakdown.

The pass rate includes `PASS` and `PASS_WITH_WARNING`; it is `100.0` when no runs exist. The repository, service, contract, edge, and discrepancy fields are currently service-provided summary values, while run totals and breakdown counts are read from JPA.

### Contract catalog

`GET /api/contracts`

Returns a fixed list of OpenAPI and AsyncAPI `ContractItemDto` entries for `impact-analysis-springboot` and `impact-analysis-engine`.

## Dependency Graph

Base path: `/api/graph`

The graph endpoints currently serve a controller-initialized sample CIR-style graph for Spring Boot, React, contracts, and events; they are not database-backed.

| Method and path | Parameters | Response |
|---|---|---|
| `GET /api/graph` | `repository`, `type` | `GraphQueryResponse` containing filtered nodes and edges |
| `GET /api/graph/nodes` | `query`, `type`, `repository`, `language` | Matching graph node maps |
| `GET /api/graph/upstream` | `nodeId` required, `maxDepth` default `2` | `GraphQueryResponse` of upstream nodes and edges |
| `GET /api/graph/downstream` | `nodeId` required, `maxDepth` default `3` | `GraphQueryResponse` of downstream nodes and edges |

Graph node maps include `id`, `name`, `type`, `repository`, `language`, `filePath`, and `lineStart`. Edge maps include `source`, `target`, `type`, `nature`, `confidence`, `evidence`, and `isCrossRepository`.

## Governance

Base path: `/api/governance`

| Method and path | Response |
|---|---|
| `GET /api/governance/status` | Fixed enterprise policy status |
| `GET /api/governance/integrations` | Fixed integration health list with masked credentials |
| `GET /api/governance/audit` | Fixed, hash-chained-style audit-event list with timestamped entries |

These endpoints are controller-generated sample responses on Stage and are not backed by a policy engine or persistent audit store.

## Error Responses

`ErrorResponse` fields:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `fieldErrors` (validation failures)

| Status | Trigger |
|---|---|
| `400` | Bean validation failure, null registration request, or `IllegalArgumentException` |
| `404` | Customer or analysis run not found |
| `409` | Duplicate customer email |
| `500` | Uncaught server exception |

## Documentation and Operations

- `GET /v3/api-docs` — raw OpenAPI JSON
- `GET /swagger-ui.html` — Swagger UI
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`
- `GET /actuator/prometheus`
