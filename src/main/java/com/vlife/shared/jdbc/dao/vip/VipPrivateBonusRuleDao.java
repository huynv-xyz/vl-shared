package com.vlife.shared.jdbc.dao.vip;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.vip.VipPrivateBonusRule;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class VipPrivateBonusRuleDao extends BaseDao<VipPrivateBonusRule, Integer> {

    public VipPrivateBonusRuleDao(JdbcClient jdbc) {
        super(jdbc, VipPrivateBonusRule.class);
    }

    public Optional<VipPrivateBonusRule> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("code", "code", code);

        return queryOne(sb);
    }

    public Page<VipPrivateBonusRule> search(@Nullable String keyword,
                                            @Nullable Integer status,
                                            Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("("
                            + "code LIKE CONCAT('%', :kw, '%') "
                            + "OR name LIKE CONCAT('%', :kw, '%') "
                            + "OR note LIKE CONCAT('%', :kw, '%')"
                            + ")")
                    .param("kw", keyword.trim());
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public boolean existsByCode(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE code = :code",
                Map.of("code", code)
        );

        return cnt != null && cnt > 0;
    }

    public boolean existsByCodeAndIdNot(String code, Integer id) {
        if (code == null || code.isBlank() || id == null) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE code = :code"
                        + " AND " + meta.id().column() + " <> :id",
                Map.of(
                        "code", code,
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

    public VipPrivateBonusRule create(VipPrivateBonusRule entity) {
        normalizeForInsert(entity);
        return super.insert(entity);
    }

    public int updateSelective(Integer id, VipPrivateBonusRule entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateSelective(id, entity);
    }

    public int updateAll(Integer id, VipPrivateBonusRule entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateAll(id, entity);
    }

    private void normalizeForInsert(VipPrivateBonusRule e) {
        if (e.getAmount() == null) e.setAmount(0D);
        if (e.getStatus() == null) e.setStatus(1);

        if (e.getCreatedAt() == null) e.setCreatedAt(java.time.LocalDateTime.now());
        if (e.getUpdatedAt() == null) e.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private void normalizeForUpdate(VipPrivateBonusRule e) {
        if (e == null) {
            return;
        }

        if (e.getStatus() == null) {
            e.setStatus(1);
        }
    }
}