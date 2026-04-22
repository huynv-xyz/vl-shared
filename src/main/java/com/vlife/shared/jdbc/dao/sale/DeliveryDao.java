package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.Delivery;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class DeliveryDao extends BaseDao<Delivery, Integer> {

    public DeliveryDao(JdbcClient jdbc) {
        super(jdbc, Delivery.class);
    }

    public Optional<Delivery> findByDeliveryNo(String deliveryNo) {
        if (deliveryNo == null || deliveryNo.isBlank()) return Optional.empty();
        return queryOne(SqlBuilder.where().eq("delivery_no", "deliveryNo", deliveryNo.trim()));
    }

    public Page<Delivery> search(
            String keyword,
            Integer orderId,
            String status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        var sb = SqlBuilder.where();

        sb.likeContains("delivery_no", "kw", keyword);
        sb.eq("order_id", "orderId", orderId);
        sb.eq("status", "status", status);

        if (fromDate != null) {
            sb.gte("delivery_date", "fromDate", fromDate);
        }

        if (toDate != null) {
            sb.lte("delivery_date", "toDate", toDate);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public Integer countByPrefix(String prefix) {
        return queryInt("""
            SELECT COUNT(*) 
            FROM deliveries 
            WHERE delivery_no LIKE :prefix
        """, Map.of("prefix", prefix + "%"), 0);
    }

    public List<Delivery> findByOrderId(Integer orderId) {
        String sql = baseSelectSql() + " WHERE order_id = :order_id ORDER BY id DESC";
        return queryList(sql, Map.of("order_id", orderId));
    }
}