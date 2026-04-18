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
@MappedEntity(value = "extra_bonus_audits", namingStrategy = NamingStrategies.Raw.class)
public class ExtraBonusAuditEntity implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("payroll_run_id")
    private Integer payrollRunId;

    @MappedProperty("period")
    private String period;

    @MappedProperty("sales_employee_id")
    private Integer salesEmployeeId;

    @MappedProperty("sales_employee_code")
    private String salesEmployeeCode;

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

    @MappedProperty("target_revenue")
    private Double targetRevenue;

    @MappedProperty("actual_revenue")
    private Double actualRevenue;

    @MappedProperty("completion_rate")
    private Double completionRate;

    @MappedProperty("debt_rate")
    private Double debtRate;

    @MappedProperty("excess_revenue")
    private Double excessRevenue;

    @MappedProperty("extra_bonus_pool")
    private Double extraBonusPool;

    @MappedProperty("asm_employee_id")
    private Integer asmEmployeeId;

    @MappedProperty("asm_employee_code")
    private String asmEmployeeCode;

    @MappedProperty("rm_employee_id")
    private Integer rmEmployeeId;

    @MappedProperty("rm_employee_code")
    private String rmEmployeeCode;

    @MappedProperty("bonus_split_rule_id")
    private Integer bonusSplitRuleId;

    @MappedProperty("bonus_split_rule_code")
    private String bonusSplitRuleCode;

    @MappedProperty("sales_rate")
    private Double salesRate;

    @MappedProperty("asm_rate")
    private Double asmRate;

    @MappedProperty("rm_rate")
    private Double rmRate;

    @MappedProperty("sales_bonus_amount")
    private Double salesBonusAmount;

    @MappedProperty("asm_bonus_amount")
    private Double asmBonusAmount;

    @MappedProperty("rm_bonus_amount")
    private Double rmBonusAmount;

    @MappedProperty("is_balanced")
    private Integer isBalanced;

    @MappedProperty("has_error")
    private Integer hasError;

    @MappedProperty("error_message")
    private String errorMessage;

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

    public Integer getSalesEmployeeId() {
        return salesEmployeeId;
    }

    public void setSalesEmployeeId(Integer salesEmployeeId) {
        this.salesEmployeeId = salesEmployeeId;
    }

    public String getSalesEmployeeCode() {
        return salesEmployeeCode;
    }

    public void setSalesEmployeeCode(String salesEmployeeCode) {
        this.salesEmployeeCode = salesEmployeeCode;
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

    public Double getExcessRevenue() {
        return excessRevenue;
    }

    public void setExcessRevenue(Double excessRevenue) {
        this.excessRevenue = excessRevenue;
    }

    public Double getExtraBonusPool() {
        return extraBonusPool;
    }

    public void setExtraBonusPool(Double extraBonusPool) {
        this.extraBonusPool = extraBonusPool;
    }

    public Integer getAsmEmployeeId() {
        return asmEmployeeId;
    }

    public void setAsmEmployeeId(Integer asmEmployeeId) {
        this.asmEmployeeId = asmEmployeeId;
    }

    public String getAsmEmployeeCode() {
        return asmEmployeeCode;
    }

    public void setAsmEmployeeCode(String asmEmployeeCode) {
        this.asmEmployeeCode = asmEmployeeCode;
    }

    public Integer getRmEmployeeId() {
        return rmEmployeeId;
    }

    public void setRmEmployeeId(Integer rmEmployeeId) {
        this.rmEmployeeId = rmEmployeeId;
    }

    public String getRmEmployeeCode() {
        return rmEmployeeCode;
    }

    public void setRmEmployeeCode(String rmEmployeeCode) {
        this.rmEmployeeCode = rmEmployeeCode;
    }

    public Integer getBonusSplitRuleId() {
        return bonusSplitRuleId;
    }

    public void setBonusSplitRuleId(Integer bonusSplitRuleId) {
        this.bonusSplitRuleId = bonusSplitRuleId;
    }

    public String getBonusSplitRuleCode() {
        return bonusSplitRuleCode;
    }

    public void setBonusSplitRuleCode(String bonusSplitRuleCode) {
        this.bonusSplitRuleCode = bonusSplitRuleCode;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Double getAsmRate() {
        return asmRate;
    }

    public void setAsmRate(Double asmRate) {
        this.asmRate = asmRate;
    }

    public Double getRmRate() {
        return rmRate;
    }

    public void setRmRate(Double rmRate) {
        this.rmRate = rmRate;
    }

    public Double getSalesBonusAmount() {
        return salesBonusAmount;
    }

    public void setSalesBonusAmount(Double salesBonusAmount) {
        this.salesBonusAmount = salesBonusAmount;
    }

    public Double getAsmBonusAmount() {
        return asmBonusAmount;
    }

    public void setAsmBonusAmount(Double asmBonusAmount) {
        this.asmBonusAmount = asmBonusAmount;
    }

    public Double getRmBonusAmount() {
        return rmBonusAmount;
    }

    public void setRmBonusAmount(Double rmBonusAmount) {
        this.rmBonusAmount = rmBonusAmount;
    }

    public Integer getIsBalanced() {
        return isBalanced;
    }

    public void setIsBalanced(Integer isBalanced) {
        this.isBalanced = isBalanced;
    }

    public Integer getHasError() {
        return hasError;
    }

    public void setHasError(Integer hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}