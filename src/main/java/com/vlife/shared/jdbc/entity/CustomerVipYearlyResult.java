package com.vlife.shared.jdbc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.naming.NamingStrategies;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@MappedEntity(value = "customer_vip_yearly_result", namingStrategy = NamingStrategies.Raw.class)
public class CustomerVipYearlyResult implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("calc_year")
    private Integer calcYear;

    @MappedProperty("customer_code")
    private String customerCode;

    @MappedProperty("customer_name")
    private String customerName;

    @MappedProperty("customer_type")
    private String customerType;

    @MappedProperty("region")
    private String region;

    @MappedProperty("group_code")
    private String groupCode;

    @MappedProperty("total_vip_point")
    private Double totalVipPoint;

    @MappedProperty("tier_code")
    private String tierCode;

    @MappedProperty("tier_name")
    private String tierName;

    @MappedProperty("reward_amount")
    private Double rewardAmount;

    @MappedProperty("total_reward_amount")
    private Double totalRewardAmount;

    @MappedProperty("private_bonus_amount")
    private Double privateBonusAmount;

    @MappedProperty("final_bonus_amount")
    private Double finalBonusAmount;

    @MappedProperty("next_tier_code")
    private String nextTierCode;

    @MappedProperty("next_tier_name")
    private String nextTierName;

    @MappedProperty("missing_point_to_next")
    private Double missingPointToNext;

    @MappedProperty("missing_point_message")
    private String missingPointMessage;

    @MappedProperty("status")
    private Integer status;

    @MappedProperty("note")
    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCalcYear() {
        return calcYear;
    }

    public void setCalcYear(Integer calcYear) {
        this.calcYear = calcYear;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Double getTotalVipPoint() {
        return totalVipPoint;
    }

    public void setTotalVipPoint(Double totalVipPoint) {
        this.totalVipPoint = totalVipPoint;
    }

    public String getTierCode() {
        return tierCode;
    }

    public void setTierCode(String tierCode) {
        this.tierCode = tierCode;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Double getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(Double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public Double getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(Double totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public Double getPrivateBonusAmount() {
        return privateBonusAmount;
    }

    public void setPrivateBonusAmount(Double privateBonusAmount) {
        this.privateBonusAmount = privateBonusAmount;
    }

    public Double getFinalBonusAmount() {
        return finalBonusAmount;
    }

    public void setFinalBonusAmount(Double finalBonusAmount) {
        this.finalBonusAmount = finalBonusAmount;
    }

    public String getNextTierCode() {
        return nextTierCode;
    }

    public void setNextTierCode(String nextTierCode) {
        this.nextTierCode = nextTierCode;
    }

    public String getNextTierName() {
        return nextTierName;
    }

    public void setNextTierName(String nextTierName) {
        this.nextTierName = nextTierName;
    }

    public Double getMissingPointToNext() {
        return missingPointToNext;
    }

    public void setMissingPointToNext(Double missingPointToNext) {
        this.missingPointToNext = missingPointToNext;
    }

    public String getMissingPointMessage() {
        return missingPointMessage;
    }

    public void setMissingPointMessage(String missingPointMessage) {
        this.missingPointMessage = missingPointMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}