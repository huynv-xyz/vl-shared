package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.ExtraBonusAuditEntity;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public class ExtraBonusAuditDao extends BaseDao<ExtraBonusAuditEntity, Integer> {

    public ExtraBonusAuditDao(JdbcClient jdbc) {
        super(jdbc, ExtraBonusAuditEntity.class);
    }

    public List<ExtraBonusAuditEntity> findByPayrollRunId(Integer payrollRunId) {
        if (payrollRunId == null) {
            return List.of();
        }

        return queryList(
                SqlBuilder.where()
                        .eq("payroll_run_id", "payrollRunId", payrollRunId)
                        .orderBy("id ASC")
        );
    }

    public int deleteByPayrollRunId(Integer payrollRunId) {
        if (payrollRunId == null) {
            return 0;
        }

        String sql = "DELETE FROM " + meta.table() + " WHERE payroll_run_id = :payrollRunId";
        return jdbc.update(sql, Map.of("payrollRunId", payrollRunId));
    }
}