package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.DeliveryItem;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public class DeliveryItemDao extends BaseDao<DeliveryItem, Integer> {

    public DeliveryItemDao(JdbcClient jdbc) {
        super(jdbc, DeliveryItem.class);
    }

    public List<DeliveryItem> findByDeliveryId(Integer deliveryId) {
        String sql = baseSelectSql() + " WHERE delivery_id = :delivery_id";
        return queryList(sql, Map.of("delivery_id", deliveryId));
    }

    // 🔥 ADD SEARCH
    public Page<DeliveryItem> search(
            Integer deliveryId,
            Integer productId,
            Pageable pageable
    ) {
        var sb = SqlBuilder.where();

        sb.eq("delivery_id", "deliveryId", deliveryId);
        sb.eq("product_id", "productId", productId);

        return queryPage(sb, pageable, "id DESC");
    }
}