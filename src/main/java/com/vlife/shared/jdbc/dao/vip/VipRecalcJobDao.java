package com.vlife.shared.jdbc.dao.vip;

import com.vlife.shared.constant.VipRecalcJobStatus;
import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.vip.VipRecalcJob;
import com.vlife.shared.jdbc.util.SqlBuilder;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Singleton
public class VipRecalcJobDao extends BaseDao<VipRecalcJob, Integer> {

    public VipRecalcJobDao(JdbcClient jdbc) {
        super(jdbc, VipRecalcJob.class);
    }

    public Optional<VipRecalcJob> findFirstPending() {
        SqlBuilder sb = SqlBuilder.where()
                .eq("status", "status", VipRecalcJobStatus.PENDING)
                .orderBy("id ASC");

        return queryOne(sb);
    }

    public Optional<VipRecalcJob> findFirstPendingByYear(Integer calcYear) {
        if (calcYear == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("calc_year", "calcYear", calcYear)
                .eq("status", "status", VipRecalcJobStatus.PENDING)
                .orderBy("id ASC")
                .limit(1);

        return queryOne(sb);
    }

    public boolean existsPendingOrProcessing(Integer calcYear) {
        if (calcYear == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) " +
                "FROM " + meta.table() + " " +
                "WHERE calc_year = :calcYear " +
                "AND status IN (:pending, :processing)";

        Long cnt = jdbc.queryLong(sql, Map.of(
                "calcYear", calcYear,
                "pending", VipRecalcJobStatus.PENDING,
                "processing", VipRecalcJobStatus.PROCESSING
        ));
        return cnt != null && cnt > 0;
    }

    public boolean existsPendingOrProcessing(Integer fromProcessMonth, Integer toProcessMonth) {
        if (fromProcessMonth == null || toProcessMonth == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) " +
                "FROM " + meta.table() + " " +
                "WHERE from_process_month = :fromProcessMonth " +
                "AND to_process_month = :toProcessMonth " +
                "AND status IN (:pending, :processing)";

        Long cnt = jdbc.queryLong(sql, Map.of(
                "fromProcessMonth", fromProcessMonth,
                "toProcessMonth", toProcessMonth,
                "pending", VipRecalcJobStatus.PENDING,
                "processing", VipRecalcJobStatus.PROCESSING
        ));
        return cnt != null && cnt > 0;
    }

    public int markProcessing(Integer id) {
        if (id == null) {
            return 0;
        }

        String sql = "UPDATE " + meta.table() + " " +
                "SET status = :status, " +
                "    started_at = :startedAt, " +
                "    updated_at = :updatedAt " +
                "WHERE id = :id " +
                "AND status = :pendingStatus";

        LocalDateTime now = LocalDateTime.now();

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", VipRecalcJobStatus.PROCESSING,
                "pendingStatus", VipRecalcJobStatus.PENDING,
                "startedAt", now,
                "updatedAt", now
        ));
    }

    public int markDone(Integer id) {
        if (id == null) {
            return 0;
        }

        String sql = "UPDATE " + meta.table() + " " +
                "SET status = :status, " +
                "    finished_at = :finishedAt, " +
                "    error_message = NULL, " +
                "    updated_at = :updatedAt " +
                "WHERE id = :id";

        LocalDateTime now = LocalDateTime.now();

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", VipRecalcJobStatus.DONE,
                "finishedAt", now,
                "updatedAt", now
        ));
    }

    public int markFailed(Integer id, String errorMessage) {
        if (id == null) {
            return 0;
        }

        String sql = "UPDATE " + meta.table() + " " +
                "SET status = :status, " +
                "    retry_count = COALESCE(retry_count, 0) + 1, " +
                "    error_message = :errorMessage, " +
                "    finished_at = :finishedAt, " +
                "    updated_at = :updatedAt " +
                "WHERE id = :id";

        LocalDateTime now = LocalDateTime.now();

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", VipRecalcJobStatus.FAILED,
                "errorMessage", errorMessage,
                "finishedAt", now,
                "updatedAt", now
        ));
    }

    public int requeue(Integer id, String errorMessage) {
        if (id == null) {
            return 0;
        }

        String sql = "UPDATE " + meta.table() + " " +
                "SET status = :status, " +
                "    retry_count = COALESCE(retry_count, 0) + 1, " +
                "    error_message = :errorMessage, " +
                "    started_at = NULL, " +
                "    finished_at = NULL, " +
                "    updated_at = :updatedAt " +
                "WHERE id = :id";

        return jdbc.update(sql, Map.of(
                "id", id,
                "status", VipRecalcJobStatus.PENDING,
                "errorMessage", errorMessage,
                "updatedAt", LocalDateTime.now()
        ));
    }

    public int deletePendingByYear(Integer calcYear) {
        if (calcYear == null) {
            return 0;
        }

        String sql = "DELETE FROM " + meta.table() + " " +
                "WHERE calc_year = :calcYear " +
                "AND status = :status";

        return jdbc.update(sql, Map.of(
                "calcYear", calcYear,
                "status", VipRecalcJobStatus.PENDING
        ));
    }

    public int deletePendingByRange(Integer fromProcessMonth, Integer toProcessMonth) {
        if (fromProcessMonth == null || toProcessMonth == null) {
            return 0;
        }

        String sql = "DELETE FROM " + meta.table() + " " +
                "WHERE from_process_month = :fromProcessMonth " +
                "AND to_process_month = :toProcessMonth " +
                "AND status = :status";

        return jdbc.update(sql, Map.of(
                "fromProcessMonth", fromProcessMonth,
                "toProcessMonth", toProcessMonth,
                "status", VipRecalcJobStatus.PENDING
        ));
    }

    public Optional<VipRecalcJob> findLatestByYear(Integer calcYear) {
        if (calcYear == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("calc_year", "calcYear", calcYear)
                .orderBy("id DESC")
                .limit(1);

        return queryOne(sb);
    }

    public Optional<VipRecalcJob> findLatestByRange(Integer fromProcessMonth, Integer toProcessMonth) {
        if (fromProcessMonth == null || toProcessMonth == null) {
            return Optional.empty();
        }

        SqlBuilder sb = SqlBuilder.where()
                .eq("from_process_month", "fromProcessMonth", fromProcessMonth)
                .eq("to_process_month", "toProcessMonth", toProcessMonth)
                .orderBy("id DESC")
                .limit(1);

        return queryOne(sb);
    }
}