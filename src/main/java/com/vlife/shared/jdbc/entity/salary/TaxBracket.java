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
@MappedEntity(value = "tax_brackets", namingStrategy = NamingStrategies.Raw.class)
public class TaxBracket implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("bracket_no")
    private Integer bracketNo;

    @MappedProperty("income_from")
    private Double incomeFrom;

    @MappedProperty("income_to")
    private Double incomeTo;

    @MappedProperty("tax_rate")
    private Double taxRate;

    @MappedProperty("quick_deduction")
    private Double quickDeduction;

    @MappedProperty("effective_from")
    private LocalDate effectiveFrom;

    @MappedProperty("effective_to")
    private LocalDate effectiveTo;

    @MappedProperty("status")
    private Integer status;

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

    public Integer getBracketNo() {
        return bracketNo;
    }

    public void setBracketNo(Integer bracketNo) {
        this.bracketNo = bracketNo;
    }

    public Double getIncomeFrom() {
        return incomeFrom;
    }

    public void setIncomeFrom(Double incomeFrom) {
        this.incomeFrom = incomeFrom;
    }

    public Double getIncomeTo() {
        return incomeTo;
    }

    public void setIncomeTo(Double incomeTo) {
        this.incomeTo = incomeTo;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getQuickDeduction() {
        return quickDeduction;
    }

    public void setQuickDeduction(Double quickDeduction) {
        this.quickDeduction = quickDeduction;
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