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
@MappedEntity(value = "role_rates", namingStrategy = NamingStrategies.Raw.class)
public class RoleRate implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("role_id")
    private Integer roleId;

    @MappedProperty("salary_rate")
    private Double salaryRate;

    @MappedProperty("bonus_rate")
    private Double bonusRate;

    @MappedProperty("basic_salary_rate")
    private Double basicSalaryRate;

    @MappedProperty("allowance_rate")
    private Double allowanceRate;

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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Double getSalaryRate() {
        return salaryRate;
    }

    public void setSalaryRate(Double salaryRate) {
        this.salaryRate = salaryRate;
    }

    public Double getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(Double bonusRate) {
        this.bonusRate = bonusRate;
    }

    public Double getBasicSalaryRate() {
        return basicSalaryRate;
    }

    public void setBasicSalaryRate(Double basicSalaryRate) {
        this.basicSalaryRate = basicSalaryRate;
    }

    public Double getAllowanceRate() {
        return allowanceRate;
    }

    public void setAllowanceRate(Double allowanceRate) {
        this.allowanceRate = allowanceRate;
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