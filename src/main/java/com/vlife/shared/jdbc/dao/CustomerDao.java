package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.dao.base.BaseDao;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.entity.Customer;
import com.vlife.shared.jdbc.util.SqlBuilder;

import java.util.Map;
import java.util.Optional;

@Singleton
public class CustomerDao extends BaseDao<Customer, Integer> {

    public CustomerDao(JdbcClient jdbc) {
        super(jdbc, Customer.class);
    }

    public Optional<Customer> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return queryOne(SqlBuilder.where().eq("code", "code", code));
    }

    public Page<Customer> search(@Nullable String keyword,
                                 @Nullable String type,
                                 @Nullable String region,
                                 @Nullable Integer status,
                                 Pageable pageable) {
        var sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("(code LIKE CONCAT('%', :kw, '%') " +
                            "OR name LIKE CONCAT('%', :kw, '%') " +
                            "OR note LIKE CONCAT('%', :kw, '%'))")
                    .param("kw", keyword.trim());
        }

        if (type != null && !type.isBlank()) {
            sb.eq("type", "type", type);
        }

        if (region != null && !region.isBlank()) {
            sb.eq("region", "region", region);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public boolean existsByCode(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }

        Long cnt = jdbc.queryLong(
                "SELECT COUNT(*) FROM " + meta.table() + " WHERE code = :code",
                Map.of("code", code)
        );
        return cnt != null && cnt > 0;
    }

    public int updateStatus(int id, int status) {
        String sql = "UPDATE " + meta.table()
                + " SET status = :st, updated_at = NOW() WHERE " + meta.id().column() + " = :id";
        return jdbc.update(sql, Map.of("id", id, "st", status));
    }
}