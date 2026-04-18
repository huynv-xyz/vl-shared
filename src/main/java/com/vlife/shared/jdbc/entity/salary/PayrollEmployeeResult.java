package com.vlife.shared.jdbc.entity.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.naming.NamingStrategies;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "payroll_employee_results", namingStrategy = NamingStrategies.Raw.class)
public class PayrollEmployeeResult implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("payroll_run_id")
    private Integer payrollRunId;

    @MappedProperty("period")
    private String period;

    @MappedProperty("employee_id")
    private Integer employeeId;

    @MappedProperty("employee_code")
    private String employeeCode;

    @MappedProperty("employee_name")
    private String employeeName;

    @MappedProperty("total_basic_salary")
    private Double totalBasicSalary;

    @MappedProperty("total_allowance")
    private Double totalAllowance;

    @MappedProperty("total_bonus")
    private Double totalBonus;

    @MappedProperty("extra_bonus_amount")
    private Double extraBonusAmount;

    @MappedProperty("gross_total")
    private Double grossTotal;

    @MappedProperty("taxable_income_before_deduction")
    private Double taxableIncomeBeforeDeduction;

    @MappedProperty("personal_deduction_amount")
    private Double personalDeductionAmount;

    @MappedProperty("dependent_deduction_amount")
    private Double dependentDeductionAmount;

    @MappedProperty("taxable_income")
    private Double taxableIncome;

    @MappedProperty("tax_bracket_no")
    private Integer taxBracketNo;

    @MappedProperty("tax_rate")
    private Double taxRate;

    @MappedProperty("quick_deduction_amount")
    private Double quickDeductionAmount;

    @MappedProperty("insurance_amount")
    private Double insuranceAmount;

    @MappedProperty("union_fee_amount")
    private Double unionFeeAmount;

    @MappedProperty("personal_income_tax_amount")
    private Double personalIncomeTaxAmount;

    @MappedProperty("net_total")
    private Double netTotal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
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