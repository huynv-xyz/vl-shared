package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.InventoryLedger;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.Map;

@Singleton
public class InventoryLedgerDao extends BaseDao<InventoryLedger, Integer> {

    public InventoryLedgerDao(JdbcClient jdbc) {
        super(jdbc, InventoryLedger.class);
    }

    public BigDecimal getStock(Integer productId, Integer warehouseId) {

        String sql = """

            SELECT COALESCE(SUM(quantity_change), 0)

            FROM inventory_ledger

            WHERE product_id = :productId

              AND warehouse_id = :warehouseId

        """;

        return jdbc.queryOne(sql, Map.of(

                "productId", productId,

                "warehouseId", warehouseId

        ), rs -> rs.getBigDecimal(1)).orElse(BigDecimal.ZERO);

    }
}