package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.Order;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Singleton
public class OrderDao extends BaseDao<Order, Integer> {

    public OrderDao(JdbcClient jdbc) {
        super(jdbc, Order.class);
    }

    public Optional<Order> findByOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isBlank()) return Optional.empty();
        return queryOne(SqlBuilder.where().eq("order_no", "orderNo", orderNo.trim()));
    }

    public Page<Order> search(
            String keyword,
            Integer customerId,
            Integer employeeId,
            String status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        var sb = SqlBuilder.where();

        sb.likeContains("order_no", "kw", keyword);

        sb.eq("customer_id", "customerId", customerId);
        sb.eq("employee_id", "employeeId", employeeId);
        sb.eq("status", "status", status);

        if (fromDate != null) {
            sb.gte("order_date", "fromDate", fromDate);
        }

        if (toDate != null) {
            sb.lte("order_date", "toDate", toDate);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public Integer countByPrefix(String prefix) {
        return queryInt("""
        SELECT COUNT(*) 
        FROM orders 
        WHERE order_no LIKE :prefix
    """, Map.of("prefix", prefix + "%"), 0);
    }
}