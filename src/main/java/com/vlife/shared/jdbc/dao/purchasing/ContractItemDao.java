package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.ContractItem;
import com.vlife.shared.jdbc.util.SqlBuilder;
import com.vlife.shared.util.CommonUtil;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ContractItemDao extends BaseDao<ContractItem, Integer> {

    public ContractItemDao(JdbcClient jdbc) {
        super(jdbc, ContractItem.class);
    }

    public Page<ContractItem> search(Integer contractId, String keyword, Pageable pageable) {

        var sb = SqlBuilder.where();

        if (contractId != null) {
            sb.eq("ci.contract_id", "contractId", contractId);
        }

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("""
            (
                p.code LIKE CONCAT('%', :kw, '%')
                OR p.name LIKE CONCAT('%', :kw, '%')
            )
        """).param("kw", keyword.trim());
        }

        String baseSql = """
        FROM contract_items ci
        LEFT JOIN products p ON p.id = ci.product_id
    """;

        // ===== COUNT =====
        long total = jdbc.queryLong(
                "SELECT COUNT(*) " + baseSql + sb.sql(),
                sb.params()
        );

        if (total == 0) {
            return Page.empty();
        }

        // ===== DATA =====
        sb.orderBy("ci.id DESC")
                .limit(pageable.getSize())
                .offset(pageable.getOffset());

        List<ContractItem> items = jdbc.queryList(
                "SELECT ci.* " + baseSql + sb.sql(),
                sb.params(),
                mapper
        );

        return Page.of(items, pageable, total);
    }

    public int deleteAllByContractId(Integer contractId) {
        if (contractId == null) return 0;

        return jdbc.update(
                "DELETE FROM contract_items WHERE contract_id = :contractId",
                java.util.Map.of("contractId", contractId)
        );
    }

    public List<ContractItem> findByContractId(Integer contractId) {
        return queryList(SqlBuilder.where().eq("contract_id", "contractId", contractId));
    }

    public List<ContractItem> findByContractIds(Collection<Integer> contractIds) {
        if (CommonUtil.isNullOrEmpty(contractIds)) return List.of();

        return queryList(
                "SELECT * FROM contract_items WHERE contract_id IN (:ids)",
                Map.of("ids", contractIds)
        );
    }

    public Map<Integer, List<ContractItem>> findByContractIdsGrouped(Set<Integer> contractIds) {

        if (CommonUtil.isNullOrEmpty(contractIds)) {
            return Collections.emptyMap();
        }

        List<ContractItem> list = queryList(
                SqlBuilder.where()
                        .in("contract_id", "contractIds", contractIds)
        );

        return list.stream()
                .collect(Collectors.groupingBy(ContractItem::getContractId));
    }

    public Map<Integer, Map<Integer, ContractItem>> findMapByContractIds(Set<Integer> contractIds) {

        if (CommonUtil.isNullOrEmpty(contractIds)) {
            return Collections.emptyMap();
        }

        List<ContractItem> list = queryList(
                SqlBuilder.where()
                        .in("contract_id", "contractIds", contractIds)
        );

        return list.stream()
                .collect(Collectors.groupingBy(
                        ContractItem::getContractId,
                        Collectors.toMap(
                                ContractItem::getProductId,
                                x -> x,
                                (a, b) -> a // tránh duplicate crash
                        )
                ));
    }
}