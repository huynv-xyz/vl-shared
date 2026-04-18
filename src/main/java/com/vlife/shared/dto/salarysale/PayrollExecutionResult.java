package com.vlife.shared.dto.salarysale;

import java.util.List;

public class PayrollExecutionResult {

    private Integer payrollRunId;
    private Integer period;

    private List<PayrollScopeCalcItem> scopeItems;
    private List<PayrollScopeResult> scopeResults;
    private List<PayrollEmployeeResult> employeeResults;

    public Integer getPayrollRunId() {
        return payrollRunId;
    }

    public void setPayrollRunId(Integer payrollRunId) {
        this.payrollRunId = payrollRunId;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public List<PayrollScopeCalcItem> getScopeItems() {
        return scopeItems;
    }

    public void setScopeItems(List<PayrollScopeCalcItem> scopeItems) {
        this.scopeItems = scopeItems;
    }

    public List<PayrollScopeResult> getScopeResults() {
        return scopeResults;
    }

    public void setScopeResults(List<PayrollScopeResult> scopeResults) {
        this.scopeResults = scopeResults;
    }

    public List<PayrollEmployeeResult> getEmployeeResults() {
        return employeeResults;
    }

    public void setEmployeeResults(List<PayrollEmployeeResult> employeeResults) {
        this.employeeResults = employeeResults;
    }
}