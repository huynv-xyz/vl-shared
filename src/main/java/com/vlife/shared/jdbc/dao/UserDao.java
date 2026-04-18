package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.User;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class UserDao extends BaseDao<User, Long> {

    public UserDao(JdbcClient jdbc) {
        super(jdbc, User.class);
    }

    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return queryOne(
                SqlBuilder.where().eq("email", "email", email.trim())
        );
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        Long cnt = jdbc.queryLong(
                "SELECT COUNT(*) FROM " + meta.table() + " WHERE email = :email",
                Map.of("email", email.trim())
        );
        return cnt != null && cnt > 0;
    }

    public Page<User> search(@Nullable String keyword,
                             @Nullable Integer status,
                             Pageable pageable) {

        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", status);

        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();
            sb.raw("(email LIKE CONCAT('%', :keyword, '%') OR name LIKE CONCAT('%', :keyword, '%'))")
                    .param("keyword", kw);
        }

        Long total = jdbc.queryLong(
                "SELECT COUNT(*) FROM " + meta.table() + sb.sql(),
                sb.params()
        );

        if (total == null || total == 0) {
            return Page.empty();
        }

        sb.orderBy("created_at DESC")
                .limit(pageable.getSize())
                .offset(pageable.getOffset());

        String sql = """
                SELECT
                    id,
                    email,
                    password,
                    name,
                    status,
                    created_at,
                    updated_at
                FROM
                """ + meta.table() + sb.sql();

        var items = jdbc.queryList(sql, sb.params(), mapper);
        return Page.of(items, pageable, total);
    }

    public int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }

        String sql = "UPDATE " + meta.table()
                + " SET status = :status, updated_at = NOW() WHERE " + meta.id().column() + " = :id";

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", status
        ));
    }
}