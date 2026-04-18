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
@MappedEntity(value = "manager_mappings", namingStrategy = NamingStrategies.Raw.class)
public class ManagerMapping implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("period")
    private String period;

    @MappedProperty("sales_employee_id")
    private Integer salesEmployeeId;

    @MappedProperty("asm_employee_id")
    private Integer asmEmployeeId;

    @MappedProperty("rm_employee_id")
    private Integer rmEmployeeId;

    @MappedProperty("region_id")
    private Integer regionId;

    @MappedProperty("province_id")
    private Integer provinceId;

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}