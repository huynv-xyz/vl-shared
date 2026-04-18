package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.BonusSplitRule;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.Optional;

@Singleton
public class BonusSplitRuleDao extends BaseDao<BonusSplitRule, Integer> {

    public BonusSplitRuleDao(JdbcClient jdbc) {
        super(jdbc, BonusSplitRule.class);
    }

    public Optional<BonusSplitRule> findEffectiveByHasAsm(int period, Integer hasAsm) {
        if (hasAsm == null) {
            return Optional.empty();
        }

        LocalDate refDate = toPeriodDate(period);

        SqlBuilder sb = SqlBuilder.where()
                .eq("has_asm", "hasAsm", hasAsm)
                .eq("status", "status", 1)
                .lte("effective_from", "refDate", refDate)
                .raw("(effective_to IS NULL OR effective_to >= :refDate)")
                .orderBy("effective_from DESC");

        return queryOne(sb);
    }

    public Page<BonusSplitRule> search(@Nullable Integer hasAsm,
                                       @Nullable Integer status,
                                       @Nullable Integer period,
                                       Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (hasAsm != null) {
            sb.eq("has_asm", "hasAsm", hasAsm);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        if (period != null) {
            LocalDate refDate = toPeriodDate(period);
            sb.lte("effective_from", "refDate", refDate)
                    .raw("(effective_to IS NULL OR effective_to >= :refDate)");
        }

        return queryPage(sb, pageable, "id DESC");
    }

    private LocalDate toPeriodDate(int period) {
        int year = period / 100;
        int month = period % 100;
        return LocalDate.of(year, month, 1);
    }
}