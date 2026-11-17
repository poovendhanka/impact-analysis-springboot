package com.example.impactanalysis.repository;

import com.example.impactanalysis.entity.AnalysisRunEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalysisRunRepository extends JpaRepository<AnalysisRunEntity, Long> {

    Optional<AnalysisRunEntity> findByRunId(String runId);

    @Query("SELECT r FROM AnalysisRunEntity r WHERE " +
           "(:repository IS NULL OR LOWER(r.repository) LIKE LOWER(CONCAT('%', :repository, '%'))) AND " +
           "(:severity IS NULL OR UPPER(r.severity) = UPPER(:severity)) AND " +
           "(:gateDecision IS NULL OR UPPER(r.gateDecision) = UPPER(:gateDecision)) AND " +
           "(:prNumber IS NULL OR r.prNumber = :prNumber)")
    Page<AnalysisRunEntity> findWithFilters(
            @Param("repository") String repository,
            @Param("severity") String severity,
            @Param("gateDecision") String gateDecision,
            @Param("prNumber") Integer prNumber,
            Pageable pageable
    );

    long countBySeverity(String severity);

    long countByGateDecision(String gateDecision);
}
