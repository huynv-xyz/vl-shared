package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.EmployeeScope;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;

@Singleton
public class EmployeeScopeDao extends BaseDao<EmployeeScope, Integer> {

    public EmployeeScopeDao(JdbcClient jdbc) {
        super(jdbc, EmployeeScope.class);
    }

    public List<EmployeeScope> findActiveByPeriod(int period) {
        LocalDate refDate = toPeriodDate(period);

        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", 1)
                .lte("effective_from", "refDate", refDate)
                .raw("(effective_to IS NULL OR effective_to >= :refDate)")
                .orderBy("id ASC");

        return queryList(sb);
    }

    public Page<EmployeeScope> search(@Nullable Integer period,
                                      @Nullable Integer employeeId,
                                      @Nullable Integer roleId,
                                      @Nullable Integer regionId,
                                      @Nullable Integer provinceId,
                                      @Nullable Integer status,
                                      Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (period != null) {
            LocalDate refDate = toPeriodDate(period);
            sb.lte("effective_from", "refDate", refDate)
                    .raw("(effective_to IS NULL OR effective_to >= :refDate)");
        }

        if (employeeId != null) {
            sb.eq("employee_id", "employeeId", employeeId);
        }

        if (roleId != null) {
            sb.eq("role_id", "roleId", roleId);
        }

        if (regionId != null) {
            sb.eq("region_id", "regionId", regionId);
        }

        if (provinceId != null) {
            sb.eq("province_id", "provinceId", provinceId);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    private LocalDate toPeriodDate(int period) {
        int year = period / 100;
        int month = period % 100;
        return LocalDate.of(year, month, 1);
    }
}