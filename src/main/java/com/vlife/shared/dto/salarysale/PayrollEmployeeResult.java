package com.vlife.shared.dto.salarysale;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
public class PayrollEmployeeResult {

    private Integer id;
    private Integer payrollRunId;
    private String period;

    private Integer employeeId;
    private String employeeCode;
    private String employeeName;

    private Double totalBasicSalary;
    private Double totalAllowance;
    private Double totalBonus;
    private Double extraBonusAmount;
    private Double grossTotal;

    private Double taxableIncomeBeforeDeduction;
    private Double personalDeductionAmount;
    private Double dependentDeductionAmount;
    private Double taxableIncome;
    private Integer taxBracketNo;
    private Double taxRate;
    private Double quickDeductionAmount;

    private Double insuranceAmount;
    private Double unionFeeAmount;
    private Double personalIncomeTaxAmount;
    private Double netTotal;

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

    public Double getTotalBasicSalary() {
        return totalBasicSalary;
    }

    public void setTotalBasicSalary(Double totalBasicSalary) {
        this.totalBasicSalary = totalBasicSalary;
    }

    public Double getTotalAllowance() {
        return totalAllowance;
    }

    public void setTotalAllowance(Double totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    public Double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(Double totalBonus) {
        this.totalBonus = totalBonus;
    }

    public Double getExtraBonusAmount() {
        return extraBonusAmount;
    }

    public void setExtraBonusAmount(Double extraBonusAmount) {
        this.extraBonusAmount = extraBonusAmount;
    }

    public Double getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(Double grossTotal) {
        this.grossTotal = grossTotal;
    }

    public Double getTaxableIncomeBeforeDeduction() {
        return taxableIncomeBeforeDeduction;
    }

    public void setTaxableIncomeBeforeDeduction(Double taxableIncomeBeforeDeduction) {
        this.taxableIncomeBeforeDeduction = taxableIncomeBeforeDeduction;
    }

    public Double getPersonalDeductionAmount() {
        return personalDeductionAmount;
    }

    public void setPersonalDeductionAmount(Double personalDeductionAmount) {
        this.personalDeductionAmount = personalDeductionAmount;
    }

    public Double getDependentDeductionAmount() {
        return dependentDeductionAmount;
    }

    public void setDependentDeductionAmount(Double dependentDeductionAmount) {
        this.dependentDeductionAmount = dependentDeductionAmount;
    }

    public Double getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(Double taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public Integer getTaxBracketNo() {
        return taxBracketNo;
    }

    public void setTaxBracketNo(Integer taxBracketNo) {
        this.taxBracketNo = taxBracketNo;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getQuickDeductionAmount() {
        return quickDeductionAmount;
    }

    public void setQuickDeductionAmount(Double quickDeductionAmount) {
        this.quickDeductionAmount = quickDeductionAmount;
    }

    public Double getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(Double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Double getUnionFeeAmount() {
        return unionFeeAmount;
    }

    public void setUnionFeeAmount(Double unionFeeAmount) {
        this.unionFeeAmount = unionFeeAmount;
    }

    public Double getPersonalIncomeTaxAmount() {
        return personalIncomeTaxAmount;
    }

    public void setPersonalIncomeTaxAmount(Double personalIncomeTaxAmount) {
        this.personalIncomeTaxAmount = personalIncomeTaxAmount;
    }

    public Double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(Double netTotal) {
        this.netTotal = netTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}