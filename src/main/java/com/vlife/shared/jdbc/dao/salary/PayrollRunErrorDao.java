package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.PayrollRunError;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class PayrollRunErrorDao extends BaseDao<PayrollRunError, Integer> {

    public PayrollRunErrorDao(JdbcClient jdbc) {
        super(jdbc, PayrollRunError.class);
    }

    public List<PayrollRunError> findByPayrollRunId(Integer payrollRunId) {
        if (payrollRunId == null) {
            return List.of();
        }

        return queryList(
                SqlBuilder.where()
                        .eq("payroll_run_id", "payrollRunId", payrollRunId)
                        .orderBy("id ASC")
        );
    }
}