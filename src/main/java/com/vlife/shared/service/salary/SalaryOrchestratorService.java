package com.vlife.shared.service.salary;

import jakarta.inject.Singleton;

@Singleton
public class SalaryOrchestratorService {

    private final SalaryBudgetService budgetService;
    private final SalaryScopeService scopeService;
    private final SalaryResultService resultService;

    public SalaryOrchestratorService(
            SalaryBudgetService budgetService,
            SalaryScopeService scopeService,
            SalaryResultService resultService
    ) {
        this.budgetService = budgetService;
        this.scopeService = scopeService;
        this.resultService = resultService;
    }

    public void run(String period) {
        budgetService.rebuild(period);
        scopeService.calculate(period);
        resultService.calculate(period);
    }
}