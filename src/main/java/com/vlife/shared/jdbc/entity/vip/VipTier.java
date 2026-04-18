package com.vlife.shared.jdbc.entity.vip;

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
@MappedEntity(value = "vip_tiers", namingStrategy = NamingStrategies.Raw.class)
public class VipTier implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("name")
    private String name;

    @MappedProperty("mb_b2b_point")
    private Double mbB2bPoint;

    @MappedProperty("mb_b2b_reward")
    private Double mbB2bReward;

    @MappedProperty("b2c_point")
    private Double b2cPoint;

    @MappedProperty("b2c_reward")
    private Double b2cReward;

    @MappedProperty("b2b_point")
    private Double b2bPoint;

    @MappedProperty("b2b_reward")
    private Double b2bReward;

    @MappedProperty("sort_order")
    private Integer sortOrder;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMbB2bPoint() {
        return mbB2bPoint;
    }

    public void setMbB2bPoint(Double mbB2bPoint) {
        this.mbB2bPoint = mbB2bPoint;
    }

    public Double getMbB2bReward() {
        return mbB2bReward;
    }

    public void setMbB2bReward(Double mbB2bReward) {
        this.mbB2bReward = mbB2bReward;
    }

    public Double getB2cPoint() {
        return b2cPoint;
    }

    public void setB2cPoint(Double b2cPoint) {
        this.b2cPoint = b2cPoint;
    }

    public Double getB2cReward() {
        return b2cReward;
    }

    public void setB2cReward(Double b2cReward) {
        this.b2cReward = b2cReward;
    }

    public Double getB2bPoint() {
        return b2bPoint;
    }

    public void setB2bPoint(Double b2bPoint) {
        this.b2bPoint = b2bPoint;
    }

    public Double getB2bReward() {
        return b2bReward;
    }

    public void setB2bReward(Double b2bReward) {
        this.b2bReward = b2bReward;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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