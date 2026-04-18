package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.SalesTarget;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class SalesTargetDao extends BaseDao<SalesTarget, Integer> {

    public SalesTargetDao(JdbcClient jdbc) {
        super(jdbc, SalesTarget.class);
    }

    public Optional<SalesTarget> findByEmployeeAndPeriod(Integer employeeId, int period) {
        if (employeeId == null || employeeId < 0) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("employee_id", "employeeId", employeeId)
                .eq("period", "period", period);

        return queryOne(sb);
    }

    public Page<SalesTarget> search(@Nullable Integer period,
                                    @Nullable Integer employeeId,
                                    Pageable pageable) {

        SqlBuilder sb = SqlBuilder.where();

        if (period != null) {
            sb.eq("period", "period", period);
        }

        if (employeeId != null && employeeId > 0) {
            sb.eq("employee_id", "employeeId", employeeId);
        }

        return queryPage(sb, pageable, "id DESC");
    }

}