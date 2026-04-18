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
@MappedEntity(value = "extra_bonus_allocations", namingStrategy = NamingStrategies.Raw.class)
public class ExtraBonusAllocationEntity implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("payroll_run_id")
    private Integer payrollRunId;

    @MappedProperty("period")
    private String period;

    @MappedProperty("source_sales_employee_id")
    private Integer sourceSalesEmployeeId;

    @MappedProperty("receiver_employee_id")
    private Integer receiverEmployeeId;

    @MappedProperty("receiver_role_id")
    private Integer receiverRoleId;

    @MappedProperty("bonus_split_rule_id")
    private Integer bonusSplitRuleId;

    @MappedProperty("allocation_rate")
    private Double allocationRate;

    @MappedProperty("allocation_amount")
    private Double allocationAmount;

    @MappedProperty("region_id")
    private Integer regionId;

    @MappedProperty("province_id")
    private Integer provinceId;

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