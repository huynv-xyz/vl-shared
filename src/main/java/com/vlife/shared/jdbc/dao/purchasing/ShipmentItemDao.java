package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.ShipmentItem;
import com.vlife.shared.jdbc.util.SqlBuilder;
import com.vlife.shared.util.CommonUtil;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ShipmentItemDao extends BaseDao<ShipmentItem, Integer> {

    public ShipmentItemDao(JdbcClient jdbc) {
        super(jdbc, ShipmentItem.class);
    }

    public Page<ShipmentItem> search(
            Integer contractId,
            String keyword,
            Pageable pageable
    ) {

        var sb = SqlBuilder.where();

        if (contractId != null) {
            sb.raw("""
            EXISTS (
                SELECT 1
                FROM shipments s
                WHERE s.id = shipment_id
                  AND s.contract_id = :contractId
            )
        """).param("contractId", contractId);
        }

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("""
            EXISTS (
                SELECT 1
                FROM products p
                WHERE p.id = product_id
                  AND (p.code LIKE CONCAT('%', :kw, '%')
                       OR p.name LIKE CONCAT('%', :kw, '%'))
            )
        """).param("kw", keyword.trim());
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public List<ShipmentItem> findByShipmentId(Integer shipmentId) {
        return queryList(
                SqlBuilder.where()
                        .eq("shipment_id", "shipmentId", shipmentId)
        );
    }

    public List<ShipmentItem> findByShipmentIds(Set<Integer> shipmentIds) {
        if (CommonUtil.isNullOrEmpty(shipmentIds)) {
            return List.of();
        }

        return queryList(
                SqlBuilder.where()
                        .in("shipment_id", "shipmentIds", shipmentIds)
        );
    }

    public int deleteByShipmentId(Integer shipmentId) {
        return jdbc.update(
                "DELETE FROM shipment_items WHERE shipment_id = :shipmentId",
                Map.of("shipmentId", shipmentId)
        );
    }

    public Map<Integer, BigDecimal> sumQuantityByContractId(Integer contractId) {

        String sql = """
            SELECT si.product_id, SUM(si.quantity) AS total_qty
            FROM shipment_items si
            JOIN shipments s ON s.id = si.shipment_id
            WHERE s.contract_id = :contractId
            GROUP BY si.product_id
        """;

        return jdbc.queryList(
                sql,
                Map.of("contractId", contractId),
                rs -> Map.entry(
                        rs.getInt("product_id"),
                        rs.getBigDecimal("total_qty")
                )
        ).stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }
}