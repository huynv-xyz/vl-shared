package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.Port;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class PortDao extends BaseDao<Port, Integer> {

    public PortDao(JdbcClient jdbc) {
        super(jdbc, Port.class);
    }

    public Page<Port> search(String keyword, Pageable pageable) {
        var sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("AND (code LIKE CONCAT('%', :keyword, '%') " +
                    "OR name LIKE CONCAT('%', :keyword, '%'))");
            sb.param("keyword", keyword.trim());
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public Optional<Port> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        List<Port> items = queryList(
                SqlBuilder.where()
                        .eq("code", "code", code.trim())
                        .limit(1)
        );

        return items.stream().findFirst();
    }

    public List<Port> findByIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return queryList(
                SqlBuilder.where()
                        .in("id", "ids", ids)
                        .orderBy("id DESC")
        );
    }
}