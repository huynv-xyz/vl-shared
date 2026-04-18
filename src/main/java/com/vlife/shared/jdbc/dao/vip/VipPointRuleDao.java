package com.vlife.shared.jdbc.dao.vip;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.vip.VipPointRule;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class VipPointRuleDao extends BaseDao<VipPointRule, Integer> {

    public VipPointRuleDao(JdbcClient jdbc) {
        super(jdbc, VipPointRule.class);
    }

    public Optional<VipPointRule> findMatchedRule(String vthhCon, Double value, @Nullable Integer status) {
        if (vthhCon == null || vthhCon.isBlank() || value == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("vthh_con", "vthhCon", vthhCon)
                .lte("from_value", "fromValue", value)
                .gte("to_value", "toValue", value);

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryOne(sb.orderBy("id ASC"));
    }

    public Page<VipPointRule> search(@Nullable String keyword,
                                     @Nullable String vthhCon,
                                     @Nullable Integer status,
                                     @Nullable Double fromValue,
                                     @Nullable Double toValue,
                                     Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("("
                            + "vthh_con LIKE CONCAT('%', :kw, '%') "
                            + "OR note LIKE CONCAT('%', :kw, '%')"
                            + ")")
                    .param("kw", keyword.trim());
        }

        if (vthhCon != null && !vthhCon.isBlank()) {
            sb.eq("vthh_con", "vthhCon", vthhCon);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        if (fromValue != null) {
            sb.gte("from_value", "fromValue", fromValue);
        }

        if (toValue != null) {
            sb.lte("to_value", "toValue", toValue);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public boolean existsOverlapRange(String vthhCon, Double fromValue, Double toValue, @Nullable Integer excludeId) {
        if (vthhCon == null || vthhCon.isBlank() || fromValue == null || toValue == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM " + meta.table()
                + " WHERE vthh_con = :vthhCon"
                + " AND NOT (to_value < :fromValue OR from_value > :toValue)";

        Map<String, Object> params;

        if (excludeId != null) {
            sql += " AND " + meta.id().column() + " <> :excludeId";
            params = Map.of(
                    "vthhCon", vthhCon,
                    "fromValue", fromValue,
                    "toValue", toValue,
                    "excludeId", excludeId
            );
        } else {
            params = Map.of(
                    "vthhCon", vthhCon,
                    "fromValue", fromValue,
                    "toValue", toValue
            );
        }

        Long cnt = queryLong(sql, params);
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

    public VipPointRule create(VipPointRule entity) {
        normalizeForInsert(entity);
        return super.insert(entity);
    }

    public int updateSelective(Integer id, VipPointRule entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateSelective(id, entity);
    }

    public int updateAll(Integer id, VipPointRule entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateAll(id, entity);
    }

    private void normalizeForInsert(VipPointRule e) {
        if (e.getFromValue() == null) e.setFromValue(0D);
        if (e.getToValue() == null) e.setToValue(0D);
        if (e.getHeSoMb() == null) e.setHeSoMb(0D);
        if (e.getHeSoMn() == null) e.setHeSoMn(0D);
        if (e.getStatus() == null) e.setStatus(1);

        if (e.getCreatedAt() == null) e.setCreatedAt(java.time.LocalDateTime.now());
        if (e.getUpdatedAt() == null) e.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private void normalizeForUpdate(VipPointRule e) {
        if (e == null) {
            return;
        }

        if (e.getStatus() == null) {
            e.setStatus(1);
        }
    }
}