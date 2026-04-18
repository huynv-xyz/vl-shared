package com.vlife.shared.dto.salarysale;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
public class PayrollScopeResult {

    private Integer id;
    private Integer payrollRunId;
    private String period;

    private Integer employeeId;
    private String employeeCode;
    private String employeeName;

    private Integer roleId;
    private String roleCode;
    private String roleName;

    private Integer regionId;
    private String regionCode;
    private String regionName;

    private Integer provinceId;
    private String provinceCode;
    private String provinceName;

    private Double targetIncome;
    private Double incomeAmount;
    private Double targetRevenue;
    private Double actualRevenue;
    private Double completionRate;
    private Double debtRate;

    private Double salary80Amount;
    private Double bonus20Amount;

    private Double salaryAmount;
    private Double bonusAmount;
    private Double roleSalaryPool;
    private Double roleBonusPool;
    private Double basicSalaryAmount;
    private Double allowanceAmount;
    private Double grossAmount;

    private Integer isBonusEligible;
    private String actualScopeType;
    private String actualScopeNote;

    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayrollRunId() {
        return payrollRunId;
    }

    public void setPayrollRunId(Integer payrollRunId) {
        this.payrollRunId = payrollRunId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Double getTargetIncome() {
        return targetIncome;
    }

    public void setTargetIncome(Double targetIncome) {
        this.targetIncome = targetIncome;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Double getTargetRevenue() {
        return targetRevenue;
    }

    public void setTargetRevenue(Double targetRevenue) {
        this.targetRevenue = targetRevenue;
    }

    public Double getActualRevenue() {
        return actualRevenue;
    }

    public void setActualRevenue(Double actualRevenue) {
        this.actualRevenue = actualRevenue;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Double getDebtRate() {
        return debtRate;
    }

    public void setDebtRate(Double debtRate) {
        this.debtRate = debtRate;
    }

    public Double getSalary80Amount() {
        return salary80Amount;
    }

    public void setSalary80Amount(Double salary80Amount) {
        this.salary80Amount = salary80Amount;
    }

    public Double getBonus20Amount() {
        return bonus20Amount;
    }

    public void setBonus20Amount(Double bonus20Amount) {
        this.bonus20Amount = bonus20Amount;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Double getRoleSalaryPool() {
        return roleSalaryPool;
    }

    public void setRoleSalaryPool(Double roleSalaryPool) {
        this.roleSalaryPool = roleSalaryPool;
    }

    public Double getRoleBonusPool() {
        return roleBonusPool;
    }

    public void setRoleBonusPool(Double roleBonusPool) {
        this.roleBonusPool = roleBonusPool;
    }

    public Double getBasicSalaryAmount() {
        return basicSalaryAmount;
    }

    public void setBasicSalaryAmount(Double basicSalaryAmount) {
        this.basicSalaryAmount = basicSalaryAmount;
    }

    public Double getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(Double allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Integer getIsBonusEligible() {
        return isBonusEligible;
    }

    public void setIsBonusEligible(Integer isBonusEligible) {
        this.isBonusEligible = isBonusEligible;
    }

    public String getActualScopeType() {
        return actualScopeType;
    }

    public void setActualScopeType(String actualScopeType) {
        this.actualScopeType = actualScopeType;
    }

    public String getActualScopeNote() {
        return actualScopeNote;
    }

    public void setActualScopeNote(String actualScopeNote) {
        this.actualScopeNote = actualScopeNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}