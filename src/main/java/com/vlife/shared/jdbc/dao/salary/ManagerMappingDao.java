package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.ManagerMapping;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class ManagerMappingDao extends BaseDao<ManagerMapping, Integer> {

    public ManagerMappingDao(JdbcClient jdbc) {
        super(jdbc, ManagerMapping.class);
    }

    public Optional<ManagerMapping> findByScope(int period,
                                                Integer salesEmployeeId,
                                                Integer regionId,
                                                Integer provinceId) {
        if (salesEmployeeId == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("period", "period", String.valueOf(period))
                .eq("sales_employee_id", "salesEmployeeId", salesEmployeeId);

        if (regionId != null) {
            sb.eq("region_id", "regionId", regionId);
        } else {
            sb.isNull("region_id");
        }

        if (provinceId != null) {
            sb.eq("province_id", "provinceId", provinceId);
        } else {
            sb.isNull("province_id");
        }

        return queryOne(sb);
    }

    public Page<ManagerMapping> search(@Nullable Integer period,
                                       @Nullable Integer salesEmployeeId,
                                       @Nullable Integer asmEmployeeId,
                                       @Nullable Integer rmEmployeeId,
                                       @Nullable Integer regionId,
                                       @Nullable Integer provinceId,
                                       Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (period != null) {
            sb.eq("period", "period", String.valueOf(period));
        }

        if (salesEmployeeId != null) {
            sb.eq("sales_employee_id", "salesEmployeeId", salesEmployeeId);
        }

        if (asmEmployeeId != null) {
            sb.eq("asm_employee_id", "asmEmployeeId", asmEmployeeId);
        }

        if (rmEmployeeId != null) {
            sb.eq("rm_employee_id", "rmEmployeeId", rmEmployeeId);
        }

        if (regionId != null) {
            sb.eq("region_id", "regionId", regionId);
        }

        if (provinceId != null) {
            sb.eq("province_id", "provinceId", provinceId);
        }

        return queryPage(sb, pageable, "id DESC");
    }
}