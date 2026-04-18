package com.vlife.shared.jdbc.dao.vip;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.vip.VipTier;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class VipTierDao extends BaseDao<VipTier, Integer> {

    public VipTierDao(JdbcClient jdbc) {
        super(jdbc, VipTier.class);
    }

    public Page<VipTier> search(@Nullable String keyword,
                                @Nullable Integer status,
                                Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();

        if (keyword != null && !keyword.isBlank()) {
            sb.raw("("
                            + "name LIKE CONCAT('%', :kw, '%') "
                            + "OR note LIKE CONCAT('%', :kw, '%')"
                            + ")")
                    .param("kw", keyword.trim());
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "sort_order ASC, id ASC");
    }

    public List<VipTier> findAllActiveOrdered() {
        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", 1)
                .orderBy("sort_order ASC, id ASC");

        return queryList(sb);
    }

    public Optional<VipTier> findFirstByStatusOrderBySortOrderAsc(int status) {
        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", status)
                .orderBy("sort_order ASC, id ASC");

        return queryOne(sb);
    }

    public Optional<VipTier> findNextTier(Integer currentSortOrder, Integer status) {
        if (currentSortOrder == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .gt("sort_order", "sortOrder", currentSortOrder);

        if (status != null) {
            sb.eq("status", "status", status);
        }

        sb.orderBy("sort_order ASC, id ASC");

        return queryOne(sb);
    }

    public Optional<VipTier> findByExactSortOrder(Integer sortOrder) {
        if (sortOrder == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("sort_order", "sortOrder", sortOrder);

        return queryOne(sb);
    }

    public boolean existsBySortOrder(Integer sortOrder) {
        if (sortOrder == null) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE sort_order = :sortOrder",
                Map.of("sortOrder", sortOrder)
        );

        return cnt != null && cnt > 0;
    }

    public boolean existsBySortOrderAndIdNot(Integer sortOrder, Integer id) {
        if (sortOrder == null || id == null) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE sort_order = :sortOrder"
                        + " AND " + meta.id().column() + " <> :id",
                Map.of(
                        "sortOrder", sortOrder,
                        "id", id
                )
        );

        return cnt != null && cnt > 0;
    }

    public boolean existsByName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        Long cnt = queryLong(
                "SELECT COUNT(*) FROM " + meta.table()
                        + " WHERE name = :name",
                Map.of("name", name.trim())
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

    public VipTier insert(VipTier entity) {
        normalizeForInsert(entity);
        return super.insert(entity);
    }

    public int updateSelective(Integer id, VipTier entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateSelective(id, entity);
    }

    public int updateAll(Integer id, VipTier entity, boolean normalize) {
        if (normalize) {
            normalizeForUpdate(entity);
        }
        return super.updateAll(id, entity);
    }

    private void normalizeForInsert(VipTier e) {
        if (e.getMbB2bPoint() == null) e.setMbB2bPoint(0D);
        if (e.getMbB2bReward() == null) e.setMbB2bReward(0D);
        if (e.getB2cPoint() == null) e.setB2cPoint(0D);
        if (e.getB2cReward() == null) e.setB2cReward(0D);
        if (e.getB2bPoint() == null) e.setB2bPoint(0D);
        if (e.getB2bReward() == null) e.setB2bReward(0D);
        if (e.getSortOrder() == null) e.setSortOrder(0);
        if (e.getStatus() == null) e.setStatus(1);

        if (e.getCreatedAt() == null) e.setCreatedAt(java.time.LocalDateTime.now());
        if (e.getUpdatedAt() == null) e.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private void normalizeForUpdate(VipTier e) {
        if (e == null) {
            return;
        }

        if (e.getStatus() == null) {
            e.setStatus(1);
        }
    }
}