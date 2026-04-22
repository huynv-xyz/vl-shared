package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.ArLedger;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Singleton
public class ArLedgerDao extends BaseDao<ArLedger, Integer> {

    public ArLedgerDao(JdbcClient jdbc) {
        super(jdbc, ArLedger.class);
    }

    public BigDecimal getBalance(Integer customerId) {

        String sql = """
            SELECT 
                COALESCE(SUM(debit_amount),0) - COALESCE(SUM(credit_amount),0)
            FROM ar_ledger
            WHERE customer_id = :customerId
        """;

        return jdbc.queryOne(sql, Map.of(
                "customerId", customerId
        ), rs -> rs.getBigDecimal(1)).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getOpeningBalance(Integer customerId, LocalDate fromDate) {

        String sql = """
            SELECT 
                COALESCE(SUM(debit_amount),0) - COALESCE(SUM(credit_amount),0)
            FROM ar_ledger
            WHERE customer_id = :customerId
              AND posting_date < :fromDate
        """;

        return jdbc.queryOne(sql, Map.of(
                "customerId", customerId,
                "fromDate", fromDate
        ), rs -> rs.getBigDecimal(1)).orElse(BigDecimal.ZERO);
    }

    public Map<String, Object> summaryByOrder(Integer orderId) {

        String sql = """
        SELECT 
            COALESCE(SUM(debit_amount),0) as total,
            COALESCE(SUM(credit_amount),0) as paid
        FROM ar_ledger
        WHERE order_id = :orderId
    """;

        Map<String, Object> row = jdbc.queryForMap(sql, Map.of("orderId", orderId));

        BigDecimal total = (BigDecimal) row.get("total");
        BigDecimal paid = (BigDecimal) row.get("paid");

        return Map.of(
                "total", total,
                "paid", paid,
                "remain", total.subtract(paid)
        );
    }
}