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

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "bonus_split_rules", namingStrategy = NamingStrategies.Raw.class)
public class BonusSplitRule implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("code")
    private String code;

    @MappedProperty("has_asm")
    private Integer hasAsm;

    @MappedProperty("sales_rate")
    private Double salesRate;

    @MappedProperty("asm_rate")
    private Double asmRate;

    @MappedProperty("rm_rate")
    private Double rmRate;

    @MappedProperty("effective_from")
    private LocalDate effectiveFrom;

    @MappedProperty("effective_to")
    private LocalDate effectiveTo;

    @MappedProperty("status")
    private Integer status;

    @MappedProperty("description")
    private String description;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getHasAsm() {
        return hasAsm;
    }

    public void setHasAsm(Integer hasAsm) {
        this.hasAsm = hasAsm;
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

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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