package com.vlife.shared.jdbc.dao;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.CustomerVipYearlyResult;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class CustomerVipYearlyResultDao extends BaseDao<CustomerVipYearlyResult, Integer> {

    public CustomerVipYearlyResultDao(JdbcClient jdbc) {
        super(jdbc, CustomerVipYearlyResult.class);
    }

    public Optional<CustomerVipYearlyResult> findByYearAndCustomerCode(Integer calcYear, String customerCode) {
        if (calcYear == null || customerCode == null || customerCode.isBlank()) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("calc_year", "calcYear", calcYear)
                .eq("customer_code", "customerCode", customerCode);

        return queryOne(sb);
    }

    public Page<CustomerVipYearlyResult> search(@Nullable Integer calcYear,
                                                @Nullable String keyword,
                                                @Nullable String customerType,
                                                @Nullable String region,
                                                @Nullable String groupCode,
                                                @Nullable String tierCode,
                                                @Nullable Integer status,
                                                Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (calcYear != null) {
            sb.eq("calc_year", "calcYear", calcYear);
        }

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("("
                            + "customer_code LIKE CONCAT('%', :kw, '%') "
                            + "OR customer_name LIKE CONCAT('%', :kw, '%') "
                            + "OR tier_code LIKE CONCAT('%', :kw, '%') "
                            + "OR tier_name LIKE CONCAT('%', :kw, '%') "
                            + "OR next_tier_code LIKE CONCAT('%', :kw, '%') "
                            + "OR next_tier_name LIKE CONCAT('%', :kw, '%') "
                            + "OR missing_point_message LIKE CONCAT('%', :kw, '%')"
                            + ")")
                    .param("kw", keyword.trim());
        }

        if (customerType != null && !customerType.isBlank()) {
            sb.eq("customer_type", "customerType", customerType);
        }

        if (region != null && !region.isBlank()) {
            sb.eq("region", "region", region);
        }

        if (groupCode != null && !groupCode.isBlank()) {
            sb.eq("group_code", "groupCode", groupCode);
        }

        if (tierCode != null && !tierCode.isBlank()) {
            sb.eq("tier_code", "tierCode", tierCode);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public int deleteByYear(int year) {
        String sql = "DELETE FROM " + meta.table() + " WHERE calc_year = :year";
        return jdbc.update(sql, Map.of("year", year));
    }

    public boolean existsByYearAndCustomerCode(Integer calcYear, String customerCode) {
        if (calcYear == null || customerCode == null || customerCode.isBlank()) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE calc_year = :calcYear AND customer_code = :customerCode",
                Map.of(
                        "calcYear", calcYear,
                        "customerCode", customerCode
                )
        );

        return cnt != null && cnt > 0;
    }

    public boolean existsByYearAndCustomerCodeAndIdNot(Integer calcYear, String customerCode, Integer id) {
        if (calcYear == null || customerCode == null || customerCode.isBlank() || id == null) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE calc_year = :calcYear"
                        + " AND customer_code = :customerCode"
                        + " AND " + meta.id().column() + " <> :id",
                Map.of(
                        "calcYear", calcYear,
                        "customerCode", customerCode,
                        "id", id
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

    public CustomerVipYearlyResult insert(CustomerVipYearlyResult entity) {
        normalizeForInsert(entity);
        return super.insert(entity);
    }

    public int updateSelective(Integer id, CustomerVipYearlyResult entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateSelective(id, entity);
    }

    public int updateAll(Integer id, CustomerVipYearlyResult entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateAll(id, entity);
    }

    private void normalizeForInsert(CustomerVipYearlyResult e) {
        if (e.getTotalVipPoint() == null) e.setTotalVipPoint(0D);
        if (e.getRewardAmount() == null) e.setRewardAmount(0D);
        if (e.getTotalRewardAmount() == null) e.setTotalRewardAmount(0D);
        if (e.getPrivateBonusAmount() == null) e.setPrivateBonusAmount(0D);
        if (e.getFinalBonusAmount() == null) e.setFinalBonusAmount(0D);
        if (e.getMissingPointToNext() == null) e.setMissingPointToNext(0D);
        if (e.getStatus() == null) e.setStatus(1);

        if (e.getCreatedAt() == null) e.setCreatedAt(java.time.LocalDateTime.now());
        if (e.getUpdatedAt() == null) e.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private void normalizeForUpdate(CustomerVipYearlyResult e) {
        if (e == null) {
            return;
        }

        if (e.getStatus() == null) {
            e.setStatus(1);
        }
    }
}