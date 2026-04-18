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
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "vip_recalc_jobs", namingStrategy = NamingStrategies.Raw.class)
public class VipRecalcJob implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("job_type")
    private String jobType;

    @MappedProperty("calc_year")
    private Integer calcYear;

    @MappedProperty("from_process_month")
    private Integer fromProcessMonth;

    @MappedProperty("to_process_month")
    private Integer toProcessMonth;

    @MappedProperty("trigger_source")
    private String triggerSource;

    @MappedProperty("trigger_ref_id")
    private Integer triggerRefId;

    @MappedProperty("status")
    private String status;

    @MappedProperty("retry_count")
    private Integer retryCount;

    @MappedProperty("payload")
    private String payload;

    @MappedProperty("error_message")
    private String errorMessage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("started_at")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("finished_at")
    private LocalDateTime finishedAt;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getCalcYear() {
        return calcYear;
    }

    public void setCalcYear(Integer calcYear) {
        this.calcYear = calcYear;
    }

    public Integer getFromProcessMonth() {
        return fromProcessMonth;
    }

    public void setFromProcessMonth(Integer fromProcessMonth) {
        this.fromProcessMonth = fromProcessMonth;
    }

    public Integer getToProcessMonth() {
        return toProcessMonth;
    }

    public void setToProcessMonth(Integer toProcessMonth) {
        this.toProcessMonth = toProcessMonth;
    }

    public String getTriggerSource() {
        return triggerSource;
    }

    public void setTriggerSource(String triggerSource) {
        this.triggerSource = triggerSource;
    }

    public Integer getTriggerRefId() {
        return triggerRefId;
    }

    public void setTriggerRefId(Integer triggerRefId) {
        this.triggerRefId = triggerRefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}