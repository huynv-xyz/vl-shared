package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.PayrollRun;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class PayrollRunDao extends BaseDao<PayrollRun, Integer> {

    public PayrollRunDao(JdbcClient jdbc) {
        super(jdbc, PayrollRun.class);
    }

    public List<PayrollRun> findByPeriod(String period) {
        SqlBuilder sb = SqlBuilder.where();

        if (period != null && !period.isBlank()) {
            sb.eq("period", "period", period.trim());
        }

        sb.orderBy("id DESC");
        return queryList(sb);
    }
}