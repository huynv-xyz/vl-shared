package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.SalesTransaction;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class SalesTransactionDao extends BaseDao<SalesTransaction, Integer> {

    public SalesTransactionDao(JdbcClient jdbc) {
        super(jdbc, SalesTransaction.class);
    }

    public int deleteAllData() {
        String sql = "DELETE FROM " + meta.table();
        return jdbc.update(sql, Map.of());
    }

    public int truncateAll() {
        String sql = "TRUNCATE TABLE " + meta.table();
        return jdbc.update(sql, Map.of());
    }

    public Optional<SalesTransaction> findByDocumentNo(String documentNo) {
        if (documentNo == null || documentNo.isBlank()) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("document_no", "documentNo", documentNo);

        return queryOne(sb);
    }

    public Page<SalesTransaction> search(@Nullable String keyword,
                                         @Nullable String customerCode,
                                         @Nullable String productCode,
                                         @Nullable String privateCode,
                                         @Nullable String customerType,
                                         @Nullable String region,
                                         @Nullable String hdnStatus,
                                         @Nullable Integer processMonth,
                                         @Nullable Integer status,
                                         @Nullable LocalDateTime documentDateFrom,
                                         @Nullable LocalDateTime documentDateTo,
                                         Pageable pageable) {

        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("("
                            + "document_no LIKE CONCAT('%', :kw, '%') "
                            + "OR customer_code LIKE CONCAT('%', :kw, '%') "
                            + "OR customer_name LIKE CONCAT('%', :kw, '%') "
                            + "OR product_code LIKE CONCAT('%', :kw, '%') "
                            + "OR product_name LIKE CONCAT('%', :kw, '%') "
                            + "OR sale_user_code LIKE CONCAT('%', :kw, '%') "
                            + "OR sale_user_name LIKE CONCAT('%', :kw, '%') "
                            + "OR private_code LIKE CONCAT('%', :kw, '%') "
                            + "OR valid_code LIKE CONCAT('%', :kw, '%') "
                            + "OR description LIKE CONCAT('%', :kw, '%')"
                            + ")")
                    .param("kw", keyword.trim());
        }

        if (customerCode != null && !customerCode.isBlank()) {
            sb.eq("customer_code", "customerCode", customerCode);
        }

        if (productCode != null && !productCode.isBlank()) {
            sb.eq("product_code", "productCode", productCode);
        }

        if (privateCode != null && !privateCode.isBlank()) {
            sb.eq("private_code", "privateCode", privateCode);
        }

        if (customerType != null && !customerType.isBlank()) {
            sb.eq("customer_type", "customerType", customerType);
        }

        if (region != null && !region.isBlank()) {
            sb.eq("region", "region", region);
        }

        if (hdnStatus != null && !hdnStatus.isBlank()) {
            sb.eq("hdn_status", "hdnStatus", hdnStatus);
        }

        if (processMonth != null) {
            sb.eq("process_month", "processMonth", processMonth);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        if (documentDateFrom != null) {
            sb.gte("document_date", "documentDateFrom", documentDateFrom);
        }

        if (documentDateTo != null) {
            sb.lt("document_date", "documentDateTo", documentDateTo);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public List<SalesTransaction> getByCustomerCodeAndProcessMonthRange(
            String customerCode,
            int fromProcessMonth,
            int toProcessMonth
    ) {
        if (customerCode == null || customerCode.isBlank()) {
            return List.of();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("customer_code", "customerCode", customerCode)
                .gte("process_month", "fromProcessMonth", fromProcessMonth)
                .lte("process_month", "toProcessMonth", toProcessMonth)
                .eq("status", "status", 1)
                .orderBy("process_month ASC, id ASC");

        return queryList(sb);
    }

    public boolean existsByDocumentNoAndCustomerCodeAndProductCode(String documentNo,
                                                                   String customerCode,
                                                                   String productCode) {
        if (documentNo == null || documentNo.isBlank()
                || customerCode == null || customerCode.isBlank()
                || productCode == null || productCode.isBlank()) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE document_no = :documentNo"
                        + " AND customer_code = :customerCode"
                        + " AND product_code = :productCode",
                Map.of(
                        "documentNo", documentNo,
                        "customerCode", customerCode,
                        "productCode", productCode
                )
        );

        return cnt != null && cnt > 0;
    }

    public int updateStatus(int id, int status) {
        String sql = "UPDATE " + meta.table()
                + " SET status = :status, updated_at = NOW()"
                + " WHERE " + meta.id().column() + " = :id";

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", status
        ));
    }

    public SalesTransaction insert(SalesTransaction entity) {
        normalizeForInsert(entity);
        return super.insert(entity);
    }

    public int updateSelective(Integer id, SalesTransaction entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateSelective(id, entity);
    }

    public int updateAll(Integer id, SalesTransaction entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateAll(id, entity);
    }

    private void normalizeForInsert(SalesTransaction e) {
        if (e.getSaleQty() == null) e.setSaleQty(0D);
        if (e.getReturnQty() == null) e.setReturnQty(0D);
        if (e.getSlRiengTl() == null) e.setSlRiengTl(0D);
        if (e.getSlTlNhom() == null) e.setSlTlNhom(0D);
        if (e.getSlLB2c() == null) e.setSlLB2c(0D);
        if (e.getSlLB2b() == null) e.setSlLB2b(0D);
        if (e.getSlHdn() == null) e.setSlHdn(0D);
        if (e.getDiemHdn() == null) e.setDiemHdn(0D);
        if (e.getSlHdnK0MaRieng() == null) e.setSlHdnK0MaRieng(0D);
        if (e.getIsGift() == null) e.setIsGift(0);
        if (e.getProcessMonth() == null) e.setProcessMonth(0);
        if (e.getStatus() == null) e.setStatus(1);

        if (e.getCreatedAt() == null) e.setCreatedAt(java.time.LocalDateTime.now());
        if (e.getUpdatedAt() == null) e.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private void normalizeForUpdate(SalesTransaction e) {
        if (e == null) {
            return;
        }

        if (e.getStatus() == null) {
            e.setStatus(1);
        }
    }
}