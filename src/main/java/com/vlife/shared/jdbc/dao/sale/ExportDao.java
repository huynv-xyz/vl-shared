package com.vlife.shared.jdbc.dao.sale;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.sale.Export;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class ExportDao extends BaseDao<Export, Integer> {

    public ExportDao(JdbcClient jdbc) {
        super(jdbc, Export.class);
    }

    // ========================
    // FIND
    // ========================
    public Optional<Export> findByExportNo(String exportNo) {
        if (exportNo == null || exportNo.isBlank()) return Optional.empty();

        return queryOne(
                SqlBuilder.where().eq("export_no", "exportNo", exportNo.trim())
        );
    }

    public Optional<Export> findByDeliveryId(Integer deliveryId) {
        if (deliveryId == null) return Optional.empty();

        return queryOne(
                SqlBuilder.where().eq("delivery_id", "deliveryId", deliveryId)
        );
    }

    public boolean existsByDeliveryId(Integer deliveryId) {
        if (deliveryId == null) return false;

        Integer cnt = queryInt("""
            SELECT COUNT(*)
            FROM exports
            WHERE delivery_id = :deliveryId
        """, Map.of("deliveryId", deliveryId), 0);

        return cnt != null && cnt > 0;
    }

    // ========================
    // SEARCH
    // ========================
    public Page<Export> search(
            String keyword,
            Integer orderId,
            Integer deliveryId,
            Integer warehouseId,
            String status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        var sb = SqlBuilder.where();

        sb.likeContains("export_no", "kw", keyword);
        sb.eq("order_id", "orderId", orderId);
        sb.eq("delivery_id", "deliveryId", deliveryId);
        sb.eq("warehouse_id", "warehouseId", warehouseId);
        sb.eq("status", "status", status);

        if (fromDate != null) {
            sb.gte("export_date", "fromDate", fromDate);
        }

        if (toDate != null) {
            sb.lte("export_date", "toDate", toDate);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    // ========================
    // COUNT
    // ========================
    public Integer countByPrefix(String prefix) {
        return queryInt("""
            SELECT COUNT(*) 
            FROM exports 
            WHERE export_no LIKE :prefix
        """, Map.of("prefix", prefix + "%"), 0);
    }

    public List<Export> findByOrderId(Integer orderId) {
        String sql = baseSelectSql() + " WHERE order_id = :order_id ORDER BY id DESC";
        return queryList(sql, Map.of("order_id", orderId));
    }
}