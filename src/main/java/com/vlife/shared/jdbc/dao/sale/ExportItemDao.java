package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.ExportItem;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class ExportItemDao extends BaseDao<ExportItem, Integer> {

    public ExportItemDao(JdbcClient jdbc) {
        super(jdbc, ExportItem.class);
    }

    // ========================
    // FIND
    // ========================
    public List<ExportItem> findByExportId(Integer exportId) {
        if (exportId == null) return List.of();

        String sql = baseSelectSql() + " WHERE export_id = :export_id";
        return queryList(sql, Map.of("export_id", exportId));
    }

    public List<ExportItem> findByExportIds(Set<Integer> exportIds) {
        if (exportIds == null || exportIds.isEmpty()) return List.of();

        String sql = baseSelectSql() + " WHERE export_id IN (:ids)";
        return queryList(sql, Map.of("ids", exportIds));
    }

    // ========================
    // 🔥 CORE: SUM EXPORTED BY ORDER
    // ========================
    public Map<Integer, BigDecimal> sumExportedByOrderId(Integer orderId) {

        if (orderId == null) return Collections.emptyMap();

        String sql = """
            SELECT ei.product_id, SUM(ei.quantity) AS total
            FROM export_items ei
            JOIN exports e ON ei.export_id = e.id
            WHERE e.order_id = :orderId
              AND e.status = 'DONE'
            GROUP BY ei.product_id
        """;

        Map<Integer, BigDecimal> result = new HashMap<>();

        jdbc.queryList(sql, Map.of("orderId", orderId), rs -> {
            while (rs.next()) {
                result.put(
                        rs.getInt("product_id"),
                        rs.getBigDecimal("total")
                );
            }
            return null;
        });

        return result;
    }

    // ========================
    // 🔥 OPTIONAL: SUM BY DELIVERY
    // ========================
    public Map<Integer, BigDecimal> sumExportedByDeliveryId(Integer deliveryId) {

        if (deliveryId == null) return Collections.emptyMap();

        String sql = """
            SELECT ei.product_id, SUM(ei.quantity) AS total
            FROM export_items ei
            JOIN exports e ON ei.export_id = e.id
            WHERE e.delivery_id = :deliveryId
            GROUP BY ei.product_id
        """;

        Map<Integer, BigDecimal> result = new HashMap<>();

        jdbc.queryList(sql, Map.of("deliveryId", deliveryId), rs -> {
            while (rs.next()) {
                result.put(
                        rs.getInt("product_id"),
                        rs.getBigDecimal("total")
                );
            }
            return null;
        });

        return result;
    }

}