package com.vlife.shared.dto.salarysale;

import java.time.LocalDateTime;

public class ExtraBonusAllocation {

    private Integer id;
    private Integer payrollRunId;
    private String period;

    private Integer sourceSalesEmployeeId;
    private Integer receiverEmployeeId;
    private Integer receiverRoleId;
    private Integer bonusSplitRuleId;

    private Double allocationRate;
    private Double allocationAmount;

    private Integer regionId;
    private Integer provinceId;

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

    public Integer getSourceSalesEmployeeId() {
        return sourceSalesEmployeeId;
    }

    public void setSourceSalesEmployeeId(Integer sourceSalesEmployeeId) {
        this.sourceSalesEmployeeId = sourceSalesEmployeeId;
    }

    public Integer getReceiverEmployeeId() {
        return receiverEmployeeId;
    }

    public void setReceiverEmployeeId(Integer receiverEmployeeId) {
        this.receiverEmployeeId = receiverEmployeeId;
    }

    public Integer getReceiverRoleId() {
        return receiverRoleId;
    }

    public void setReceiverRoleId(Integer receiverRoleId) {
        this.receiverRoleId = receiverRoleId;
    }

    public Integer getBonusSplitRuleId() {
        return bonusSplitRuleId;
    }

    public void setBonusSplitRuleId(Integer bonusSplitRuleId) {
        this.bonusSplitRuleId = bonusSplitRuleId;
    }

    public Double getAllocationRate() {
        return allocationRate;
    }

    public void setAllocationRate(Double allocationRate) {
        this.allocationRate = allocationRate;
    }

    public Double getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(Double allocationAmount) {
        this.allocationAmount = allocationAmount;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}