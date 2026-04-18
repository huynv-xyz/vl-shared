package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.Product;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class ProductDao extends BaseDao<Product, Integer> {

    public ProductDao(JdbcClient jdbc) {
        super(jdbc, Product.class);
    }

    public Optional<Product> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return queryOne(SqlBuilder.where().eq("code", "code", code.trim()));
    }

    public Page<Product> search(@Nullable String keyword,
                                @Nullable Integer status,
                                Pageable pageable) {
        var sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("(code LIKE CONCAT('%', :kw, '%') " +
                            "OR name LIKE CONCAT('%', :kw, '%') " +
                            "OR unit LIKE CONCAT('%', :kw, '%'))")
                    .param("kw", keyword.trim());
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }
}