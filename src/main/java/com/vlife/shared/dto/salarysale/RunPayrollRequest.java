package com.vlife.shared.dto.salarysale;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class RunPayrollRequest {

    private Integer period;
    private String triggeredBy;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}