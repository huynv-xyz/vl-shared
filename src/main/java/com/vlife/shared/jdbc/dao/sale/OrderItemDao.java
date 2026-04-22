package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.OrderItem;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Singleton
public class OrderItemDao extends BaseDao<OrderItem, Integer> {

    public OrderItemDao(JdbcClient jdbc) {
        super(jdbc, OrderItem.class);
    }

    // ========================
    // FIND BY ORDER ID
    // ========================
    public List<OrderItem> findByOrderId(Integer orderId) {
        if (orderId == null) return Collections.emptyList();

        SqlBuilder sb = SqlBuilder.where()
                .eq("order_id", "orderId", orderId)
                .orderBy("id ASC");

        return queryList(sb);
    }

    // ========================
    // FIND BY MULTIPLE ORDER IDS
    // ========================
    public List<OrderItem> findByOrderIds(Set<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) return Collections.emptyList();

        SqlBuilder sb = SqlBuilder.where()
                .in("order_id", "orderIds", orderIds)
                .orderBy("order_id ASC, id ASC");

        return queryList(sb);
    }

    public int deleteByOrderId(Integer orderId) {
        if (orderId == null) return 0;

        SqlBuilder sb = SqlBuilder.where()
                .eq("order_id", "orderId", orderId);

        String sql = "DELETE FROM order_items " + sb.sql();

        return jdbc.update(sql, sb.params());
    }

    public void saveItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return;

        saveAll(items);
    }
}