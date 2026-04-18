package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.Contract;
import com.vlife.shared.jdbc.util.SqlBuilder;
import com.vlife.shared.util.CommonUtil;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Optional;

@Singleton
public class ContractDao extends BaseDao<Contract, Integer> {

    public ContractDao(JdbcClient jdbc) {
        super(jdbc, Contract.class);
    }

    public Optional<Contract> findByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        return queryOne(SqlBuilder.where().eq("code", "code", code.trim()));
    }

    public Page<Contract> search(
            String keyword,
            Integer productId,
            String productKeyword,
            Integer supplierId,
            LocalDateTime signedDateFrom,
            LocalDateTime signedDateTo,

            Pageable pageable
    ) {

        var sb = SqlBuilder.where();

        sb.likeContains("code", "kw", keyword);

        sb.eq("supplier_id", "supplierId", supplierId);

        if (signedDateFrom != null) {
            sb.gte("signed_date", "signedDateFrom", signedDateFrom);
        }

        if (signedDateTo != null) {
            sb.lte("signed_date", "signedDateTo", signedDateTo);
        }

        // ===== product filter (EXISTS)
        if (productId != null || (productKeyword != null && !productKeyword.isBlank())) {

            StringBuilder sub = new StringBuilder("""
        EXISTS (
            SELECT 1
            FROM contract_items ci
            JOIN products p ON p.id = ci.product_id
            WHERE ci.contract_id = contracts.id
    """);

            if (productId != null) {
                sub.append(" AND ci.product_id = :productId ");
                sb.param("productId", productId);
            }

            if (productKeyword != null && !productKeyword.isBlank()) {
                sub.append("""
            AND (
                p.name LIKE CONCAT('%', :pkw, '%')
                OR p.code LIKE CONCAT('%', :pkw, '%')
            )
        """);
                sb.param("pkw", productKeyword.trim());
            }

            sub.append(")");
            sb.raw(sub.toString());
        }

        return queryPage(sb, pageable, "id DESC");
    }
}