-- Flyway Migration V2: Create analysis_runs table for Phase 11 persistence
CREATE TABLE IF NOT EXISTS analysis_runs (
    id BIGSERIAL PRIMARY KEY,
    run_id VARCHAR(64) NOT NULL UNIQUE,
    repository VARCHAR(255) NOT NULL,
    commit_sha VARCHAR(64) NOT NULL,
    pr_number INTEGER,
    branch VARCHAR(128),
    severity VARCHAR(32) NOT NULL,
    gate_decision VARCHAR(32) NOT NULL,
    duration_seconds DOUBLE PRECISION DEFAULT 0.0,
    total_nodes INTEGER DEFAULT 0,
    total_edges INTEGER DEFAULT 0,
    breaking_changes_count INTEGER DEFAULT 0,
    report_markdown TEXT,
    report_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_analysis_runs_repo ON analysis_runs (repository);
CREATE INDEX IF NOT EXISTS idx_analysis_runs_severity ON analysis_runs (severity);
CREATE INDEX IF NOT EXISTS idx_analysis_runs_gate ON analysis_runs (gate_decision);
CREATE INDEX IF NOT EXISTS idx_analysis_runs_created ON analysis_runs (created_at DESC);
