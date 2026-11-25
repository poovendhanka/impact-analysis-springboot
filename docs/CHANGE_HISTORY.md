# Change History

## 2026-11-25

| Commit | Summary | Key Files/Areas Changed |
|---|---|---|
| `f92bd18` | Merge pull request #19 from `test/manualtesting` into Stage. | Merge commit |
| `e619bda` | Added a comment to the `AnalysisRunService` interface. | `src/main/java/com/example/impactanalysis/service/AnalysisRunService.java` |
| `0a9d3b2` | Added code-documentation skill instructions for maintaining repository docs. | `.github/skills/code-documentation/SKILL.md` |
| `769b89b` | Added a Stage-push and manual GitHub Actions workflow to generate documentation. | `.github/workflows/update-documentation.yml` |

## 2026-11-17

| Commit | Summary | Key Files/Areas Changed |
|---|---|---|
| `ed9eb62` | Removed an unnecessary comment from `AnalysisRunService`. | `src/main/java/com/example/impactanalysis/service/AnalysisRunService.java` |
| `2d993d9` | Renamed `getHistoryId` to `getHistory` in the analysis-run service API. | `src/main/java/com/example/impactanalysis/service/AnalysisRunService.java` |
| `bb84603` | Renamed `getHistory` to `getHistoryId` in the analysis-run service API. | `src/main/java/com/example/impactanalysis/service/AnalysisRunService.java` |
| `a2af540` | Reverted the earlier blocked-PR phone-field removal attempt. | Customer response DTO and `CustomerServiceImpl` |
| `66434f3` | Attempted a merge on a blocked pull request. | Merge commit |
| `294765a` | Removed the `phone` field from `CustomerResponse` and its service mapping as a breaking API change. | `CustomerResponse`, `CustomerServiceImpl` |
| `24244db` | Added `CustomerNotFoundException` and a matching 404 response in the global exception handler. | Customer exception handling and service lookup |
| `c6cb019` | Updated service-catalog downstream consumers, OpenAPI documentation, and impact-policy gate configuration. | `.github/workflows/impact-analysis.yml`, `impact-policy.yml`, `openapi.yaml`, `service-catalog.yml` |
| `dab2de3` | Added production deployment assets, Actuator/Prometheus observability, robust CORS, and analysis-run implementation updates. | Docker, Compose, environment template, CI, CORS, Actuator config, `pom.xml`, `AnalysisRunServiceImpl` |
| `9809904` | Implemented JPA persistence and APIs for analysis history, graph navigation, and dashboard metrics. | Analysis controllers and DTOs, JPA entity/repository/service, Flyway V2, and focused tests |

## 2026-11-16

| Commit | Summary | Key Files/Areas Changed |
|---|---|---|
| `65f5097` | Added governance status, integration-health, and audit-trail endpoints. | Governance controller and DTOs, plus governance controller tests |
