package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.Region;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class RegionDao extends BaseDao<Region, Integer> {

    public RegionDao(JdbcClient jdbc) {
        super(jdbc, Region.class);
    }

    public Optional<Region> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        return queryOne(
                SqlBuilder.where()
                        .eq("code", "code", code.trim())
        );
    }

    public List<Region> findAllActive() {
        return queryList(
                SqlBuilder.where()
                        .eq("status", "status", 1)
                        .orderBy("id ASC")
        );
    }

    public Page<Region> search(@Nullable String keyword,
                               @Nullable Integer status,
                               Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            String kw = "%" + keyword.trim() + "%";
            sb.append(
                    " AND (code LIKE :keyword OR name LIKE :keyword) ",
                    java.util.Map.of("keyword", kw)
            );
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        sb.orderBy("id DESC");

        return queryPage(sb, pageable, "id DESC");
    }
}