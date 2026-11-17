package com.example.impactanalysis.dto;

import java.util.List;

/**
 * Enterprise Governance Status DTO.
 * Exposes active policy rules, environment, and merge protection mode.
 */
public class GovernanceStatusResponse {
    private String organizationName;
    private String activeTier;
    private String environment;
    private List<String> blockOnSeverity;
    private boolean allowOverrides;
    private boolean breakingChangesBlocked;
    private List<String> mandatoryRulesEnforced;

    public GovernanceStatusResponse() {
    }

    public GovernanceStatusResponse(
            String organizationName,
            String activeTier,
            String environment,
            List<String> blockOnSeverity,
            boolean allowOverrides,
            boolean breakingChangesBlocked,
            List<String> mandatoryRulesEnforced
    ) {
        this.organizationName = organizationName;
        this.activeTier = activeTier;
        this.environment = environment;
        this.blockOnSeverity = blockOnSeverity;
        this.allowOverrides = allowOverrides;
        this.breakingChangesBlocked = breakingChangesBlocked;
        this.mandatoryRulesEnforced = mandatoryRulesEnforced;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getActiveTier() {
        return activeTier;
    }

    public void setActiveTier(String activeTier) {
        this.activeTier = activeTier;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<String> getBlockOnSeverity() {
        return blockOnSeverity;
    }

    public void setBlockOnSeverity(List<String> blockOnSeverity) {
        this.blockOnSeverity = blockOnSeverity;
    }

    public boolean isAllowOverrides() {
        return allowOverrides;
    }

    public void setAllowOverrides(boolean allowOverrides) {
        this.allowOverrides = allowOverrides;
    }

    public boolean isBreakingChangesBlocked() {
        return breakingChangesBlocked;
    }

    public void setBreakingChangesBlocked(boolean breakingChangesBlocked) {
        this.breakingChangesBlocked = breakingChangesBlocked;
    }

    public List<String> getMandatoryRulesEnforced() {
        return mandatoryRulesEnforced;
    }

    public void setMandatoryRulesEnforced(List<String> mandatoryRulesEnforced) {
        this.mandatoryRulesEnforced = mandatoryRulesEnforced;
    }
}
