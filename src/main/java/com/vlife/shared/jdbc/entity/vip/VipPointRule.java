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
@MappedEntity(value = "vip_point_rules", namingStrategy = NamingStrategies.Raw.class)
public class VipPointRule implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("vthh_con")
    private String vthhCon;

    @MappedProperty("from_value")
    private Double fromValue;

    @MappedProperty("to_value")
    private Double toValue;

    @MappedProperty("he_so_mb")
    private Double heSoMb;

    @MappedProperty("he_so_mn")
    private Double heSoMn;

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

    public String getVthhCon() {
        return vthhCon;
    }

    public void setVthhCon(String vthhCon) {
        this.vthhCon = vthhCon;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }

    public Double getHeSoMb() {
        return heSoMb;
    }

    public void setHeSoMb(Double heSoMb) {
        this.heSoMb = heSoMb;
    }

    public Double getHeSoMn() {
        return heSoMn;
    }

    public void setHeSoMn(Double heSoMn) {
        this.heSoMn = heSoMn;
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