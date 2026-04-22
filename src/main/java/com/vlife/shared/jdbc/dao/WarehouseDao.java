package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.Warehouse;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

@Singleton
public class WarehouseDao extends BaseDao<Warehouse, Integer> {

    public WarehouseDao(JdbcClient jdbc) {
        super(jdbc, Warehouse.class);
    }

    public Page<Warehouse> search(String keyword, Pageable pageable) {

        var sb = SqlBuilder.where();

        sb.likeContains("name", "kw", keyword);

        return queryPage(sb, pageable, "id DESC");
    }
}