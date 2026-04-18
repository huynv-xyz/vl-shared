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
@MappedEntity(value = "sales_targets", namingStrategy = NamingStrategies.Raw.class)
public class SalesTarget implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("period")
    private Integer period; // 202601

    @MappedProperty("employee_id")
    private Integer employeeId;

    @MappedProperty("bon_goc")
    private Double bonGoc;

    @MappedProperty("bon_la_bot")
    private Double bonLaBot;

    @MappedProperty("clcn")
    private Double clcn;

    @MappedProperty("bon_la_long")
    private Double bonLaLong;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Integer getId() { return id; }
    @Override
    public void setId(Integer id) { this.id = id; }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Double getBonGoc() {
        return bonGoc;
    }

    public void setBonGoc(Double bonGoc) {
        this.bonGoc = bonGoc;
    }

    public Double getBonLaBot() {
        return bonLaBot;
    }

    public void setBonLaBot(Double bonLaBot) {
        this.bonLaBot = bonLaBot;
    }

    public Double getClcn() {
        return clcn;
    }

    public void setClcn(Double clcn) {
        this.clcn = clcn;
    }

    public Double getBonLaLong() {
        return bonLaLong;
    }

    public void setBonLaLong(Double bonLaLong) {
        this.bonLaLong = bonLaLong;
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