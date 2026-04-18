package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.ExtraBonusAllocationEntity;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public class ExtraBonusAllocationDao extends BaseDao<ExtraBonusAllocationEntity, Integer> {

    public ExtraBonusAllocationDao(JdbcClient jdbc) {
        super(jdbc, ExtraBonusAllocationEntity.class);
    }

    public List<ExtraBonusAllocationEntity> findByPayrollRunId(Integer payrollRunId) {
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