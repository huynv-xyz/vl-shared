package com.vlife.shared.dto.salarysale;

import java.time.LocalDateTime;

public class ExtraBonusAudit {

    private Integer id;
    private Integer payrollRunId;
    private String period;

    private Integer salesEmployeeId;
    private Integer regionId;
    private Integer provinceId;

    private Double targetRevenue;
    private Double actualRevenue;
    private Double completionRate;
    private Double debtRate;
    private Double excessRevenue;
    private Double extraBonusPool;

    private Integer asmEmployeeId;
    private Integer rmEmployeeId;
    private Integer bonusSplitRuleId;

    private Double salesRate;
    private Double asmRate;
    private Double rmRate;

    private Double salesBonusAmount;
    private Double asmBonusAmount;
    private Double rmBonusAmount;

    private Integer isBalanced;
    private Integer hasError;
    private String errorMessage;

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

    public Integer getSalesEmployeeId() {
        return salesEmployeeId;
    }

    public void setSalesEmployeeId(Integer salesEmployeeId) {
        this.salesEmployeeId = salesEmployeeId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
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

    public Integer getRmEmployeeId() {
        return rmEmployeeId;
    }

    public void setRmEmployeeId(Integer rmEmployeeId) {
        this.rmEmployeeId = rmEmployeeId;
    }

    public Integer getBonusSplitRuleId() {
        return bonusSplitRuleId;
    }

    public void setBonusSplitRuleId(Integer bonusSplitRuleId) {
        this.bonusSplitRuleId = bonusSplitRuleId;
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