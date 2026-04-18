package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.ProgressiveBonusTier;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;

@Singleton
public class ProgressiveBonusTierDao extends BaseDao<ProgressiveBonusTier, Integer> {

    public ProgressiveBonusTierDao(JdbcClient jdbc) {
        super(jdbc, ProgressiveBonusTier.class);
    }

    public List<ProgressiveBonusTier> findActiveByPeriod(int period) {
        LocalDate refDate = toPeriodDate(period);

        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", 1)
                .lte("effective_from", "refDate", refDate)
                .raw("(effective_to IS NULL OR effective_to >= :refDate)")
                .orderBy("sort_order ASC");

        return queryList(sb);
    }

    public Page<ProgressiveBonusTier> search(@Nullable Integer status,
                                             @Nullable Integer period,
                                             Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (status != null) {
            sb.eq("status", "status", status);
        }

        if (period != null) {
            LocalDate refDate = toPeriodDate(period);
            sb.lte("effective_from", "refDate", refDate)
                    .raw("(effective_to IS NULL OR effective_to >= :refDate)");
        }

        return queryPage(sb, pageable, "sort_order ASC");
    }

    private LocalDate toPeriodDate(int period) {
        int year = period / 100;
        int month = period % 100;
        return LocalDate.of(year, month, 1);
    }
}