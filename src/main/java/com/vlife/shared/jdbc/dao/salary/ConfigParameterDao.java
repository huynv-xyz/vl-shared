package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.ConfigParameter;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.Optional;

@Singleton
public class ConfigParameterDao extends BaseDao<ConfigParameter, Integer> {

    public ConfigParameterDao(JdbcClient jdbc) {
        super(jdbc, ConfigParameter.class);
    }

    public Optional<ConfigParameter> findEffective(String groupCode, String code, int period) {
        if (isBlank(groupCode) || isBlank(code)) {
            return Optional.empty();
        }

        LocalDate d = toPeriodDate(period);

        String sql = baseSelectSql()
                + " WHERE group_code = :groupCode"
                + " AND code = :code"
                + " AND status = 1"
                + " AND effective_from <= :dt"
                + " AND (effective_to IS NULL OR effective_to >= :dt)"
                + " ORDER BY effective_from DESC"
                + " LIMIT 1";

        return jdbc.queryOne(
                sql,
                java.util.Map.of(
                        "groupCode", groupCode.trim(),
                        "code", code.trim(),
                        "dt", d
                ),
                mapper
        );
    }

    public Page<ConfigParameter> search(@Nullable String keyword,
                                        @Nullable String groupCode,
                                        @Nullable Integer status,
                                        Pageable pageable) {
        var sb = SqlBuilder.where();

        if (!isBlank(keyword)) {
            sb.raw("(code LIKE CONCAT('%', :kw, '%') " +
                            "OR group_code LIKE CONCAT('%', :kw, '%') " +
                            "OR description LIKE CONCAT('%', :kw, '%'))")
                    .param("kw", keyword.trim());
        }

        if (!isBlank(groupCode)) {
            sb.eq("group_code", "groupCode", groupCode.trim());
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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}