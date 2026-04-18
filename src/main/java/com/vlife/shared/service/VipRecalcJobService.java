package com.vlife.shared.service;

import com.vlife.shared.constant.VipRecalcJobStatus;
import com.vlife.shared.jdbc.dao.vip.VipRecalcJobDao;
import com.vlife.shared.jdbc.entity.vip.VipRecalcJob;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.time.Year;

@Singleton
public class VipRecalcJobService {

    public static final String JOB_TYPE_RECALC_CUSTOMER_VIP_YEAR = "RECALC_CUSTOMER_VIP_YEAR";

    private final VipRecalcJobDao vipRecalcJobDao;

    public VipRecalcJobService(VipRecalcJobDao vipRecalcJobDao) {
        this.vipRecalcJobDao = vipRecalcJobDao;
    }

    public void enqueueCurrentYearIfNeeded(String triggerSource, Integer triggerRefId) {
        int currentYear = Year.now().getValue();
        enqueueYearIfNeeded(currentYear, triggerSource, triggerRefId);
    }

    public void enqueueYearIfNeeded(int calcYear, String triggerSource, Integer triggerRefId) {
        if (vipRecalcJobDao.existsPendingOrProcessing(calcYear)) {
            return;
        }

        VipRecalcJob job = new VipRecalcJob();
        job.setJobType(JOB_TYPE_RECALC_CUSTOMER_VIP_YEAR);
        job.setCalcYear(calcYear);
        job.setTriggerSource(triggerSource);
        job.setTriggerRefId(triggerRefId);
        job.setStatus(VipRecalcJobStatus.PENDING);
        job.setRetryCount(0);

        LocalDateTime now = LocalDateTime.now();
        job.setCreatedAt(now);
        job.setUpdatedAt(now);

        vipRecalcJobDao.insert(job);
    }
}