package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.Nation;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class NationDao extends BaseDao<Nation, Integer> {

    public NationDao(JdbcClient jdbc) {
        super(jdbc, Nation.class);
    }

    public Optional<Nation> findByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        return queryOne(SqlBuilder.where().eq("code", "code", code.trim()));
    }

    public Optional<Nation> findByName(String name) {
        if (name == null || name.isBlank()) return Optional.empty();
        return queryOne(SqlBuilder.where().eq("name", "name", name.trim()));
    }

    public Page<Nation> search(String keyword, Pageable pageable) {
        var sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("(code LIKE CONCAT('%', :kw, '%') OR name LIKE CONCAT('%', :kw, '%'))")
                    .param("kw", keyword.trim());
        }

        return queryPage(sb, pageable, "id DESC");
    }
}