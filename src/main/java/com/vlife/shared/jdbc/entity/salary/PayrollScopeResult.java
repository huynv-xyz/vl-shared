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
@MappedEntity(value = "payroll_scope_results", namingStrategy = NamingStrategies.Raw.class)
public class PayrollScopeResult implements Identifiable<Integer> {

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

    @MappedProperty("role_id")
    private Integer roleId;

    @MappedProperty("role_code")
    private String roleCode;

    @MappedProperty("role_name")
    private String roleName;

    @MappedProperty("region_id")
    private Integer regionId;

    @MappedProperty("region_code")
    private String regionCode;

    @MappedProperty("region_name")
    private String regionName;

    @MappedProperty("province_id")
    private Integer provinceId;

    @MappedProperty("province_code")
    private String provinceCode;

    @MappedProperty("province_name")
    private String provinceName;

    @MappedProperty("target_income")
    private Double targetIncome;

    @MappedProperty("income_amount")
    private Double incomeAmount;

    @MappedProperty("target_revenue")
    private Double targetRevenue;

    @MappedProperty("actual_revenue")
    private Double actualRevenue;

    @MappedProperty("completion_rate")
    private Double completionRate;

    @MappedProperty("debt_rate")
    private Double debtRate;

    @MappedProperty("salary_80_amount")
    private Double salary80Amount;

    @MappedProperty("bonus_20_amount")
    private Double bonus20Amount;

    @MappedProperty("salary_amount")
    private Double salaryAmount;

    @MappedProperty("bonus_amount")
    private Double bonusAmount;

    @MappedProperty("role_salary_pool")
    private Double roleSalaryPool;

    @MappedProperty("role_bonus_pool")
    private Double roleBonusPool;

    @MappedProperty("basic_salary_amount")
    private Double basicSalaryAmount;

    @MappedProperty("allowance_amount")
    private Double allowanceAmount;

    @MappedProperty("gross_amount")
    private Double grossAmount;

    @MappedProperty("is_bonus_eligible")
    private Integer isBonusEligible;

    @MappedProperty("actual_scope_type")
    private String actualScopeType;

    @MappedProperty("actual_scope_note")
    private String actualScopeNote;

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