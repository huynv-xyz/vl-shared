package com.vlife.shared.jdbc.dao.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.salary.Role;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class RoleDao extends BaseDao<Role, Integer> {

    public RoleDao(JdbcClient jdbc) {
        super(jdbc, Role.class);
    }

    public Optional<Role> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        return queryOne(
                SqlBuilder.where().eq("code", "code", code.trim())
        );
    }

    public List<Role> findAllActive() {
        return queryList(
                SqlBuilder.where()
                        .eq("status", "status", 1)
                        .orderBy("code ASC")
        );
    }

    public Page<Role> search(@Nullable String keyword,
                             @Nullable String type,
                             @Nullable Integer status,
                             Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            String kw = "%" + keyword.trim() + "%";
            sb.append(
                    " AND (code LIKE :keyword OR name LIKE :keyword OR description LIKE :keyword) ",
                    java.util.Map.of("keyword", kw)
            );
        }

        if (type != null && !type.isBlank()) {
            sb.eq("type", "type", type.trim());
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "code ASC");
    }
}