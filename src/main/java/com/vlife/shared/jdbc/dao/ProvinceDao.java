package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.Province;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public class ProvinceDao extends BaseDao<Province, Integer> {

    public ProvinceDao(JdbcClient jdbc) {
        super(jdbc, Province.class);
    }

    public Page<Province> search(@Nullable String keyword,
                                 @Nullable Integer regionId,
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

        if (regionId != null) {
            sb.eq("region_id", "regionId", regionId);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "code ASC");
    }

    public Optional<Province> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        return queryOne(
                SqlBuilder.where()
                        .eq("code", "code", code.trim())
        );
    }

    public List<Province> findAllActive() {
        return queryList(
                SqlBuilder.where()
                        .eq("status", "status", 1)
                        .orderBy("id ASC")
        );
    }

    public List<Province> findByRegionId(Integer regionId) {
        if (regionId == null) {
            return Collections.emptyList();
        }

        return queryList(
                SqlBuilder.where()
                        .eq("region_id", "regionId", regionId)
                        .eq("status", "status", 1)
                        .orderBy("id ASC")
        );
    }


}