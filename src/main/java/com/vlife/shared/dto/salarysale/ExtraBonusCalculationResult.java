package com.vlife.shared.dto.salarysale;

import java.util.List;

public class ExtraBonusCalculationResult {

    private List<ExtraBonusAudit> audits;
    private List<ExtraBonusAllocation> allocations;
    private List<PayrollEmployeeResult> employeeResults;

    public List<ExtraBonusAudit> getAudits() {
        return audits;
    }

    public void setAudits(List<ExtraBonusAudit> audits) {
        this.audits = audits;
    }

    public List<ExtraBonusAllocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<ExtraBonusAllocation> allocations) {
        this.allocations = allocations;
    }

    public List<PayrollEmployeeResult> getEmployeeResults() {
        return employeeResults;
    }

    public void setEmployeeResults(List<PayrollEmployeeResult> employeeResults) {
        this.employeeResults = employeeResults;
    }
}