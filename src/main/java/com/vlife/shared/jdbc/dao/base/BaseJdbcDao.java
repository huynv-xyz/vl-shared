package com.vlife.shared.jdbc.dao.base;

import com.vlife.shared.jdbc.builder.SqlArg;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseJdbcDao {

    protected final JdbcOperations jdbc;

    protected BaseJdbcDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs, int rowNum) throws SQLException;
    }

    protected long count(String sql, List<SqlArg> args) {
        return jdbc.prepareStatement(sql, ps -> {
            bind(ps, args);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return 0L;
                }
                return rs.getLong(1);
            }
        });
    }

    protected <T> List<T> list(String sql, List<SqlArg> args, RowMapper<T> mapper) {
        return jdbc.prepareStatement(sql, ps -> {
            bind(ps, args);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> out = new ArrayList<>();
                int row = 0;
                while (rs.next()) {
                    out.add(mapper.map(rs, row++));
                }
                return out;
            }
        });
    }

    protected <T> T one(String sql, List<SqlArg> args, RowMapper<T> mapper) {
        return jdbc.prepareStatement(sql, ps -> {
            bind(ps, args);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapper.map(rs, 0);
            }
        });
    }

    protected int update(String sql, List<SqlArg> args) {
        return jdbc.prepareStatement(sql, ps -> {
            bind(ps, args);
            return ps.executeUpdate();
        });
    }

    protected <T> Page<T> page(
            String selectSqlNoLimitOffset,
            String countSql,
            List<SqlArg> args,
            Pageable pageable,
            RowMapper<T> mapper
    ) {
        long total = count(countSql, args);

        if (total == 0) {
            return Page.of(List.of(), pageable, 0L);
        }

        List<SqlArg> listArgs = new ArrayList<>(args);
        listArgs.add(SqlArg.val(pageable.getSize()));
        listArgs.add(SqlArg.val((int) pageable.getOffset()));

        String select = selectSqlNoLimitOffset + " LIMIT ? OFFSET ?";
        List<T> items = list(select, listArgs, mapper);

        return Page.of(items, pageable, total);
    }

    protected void bind(PreparedStatement ps, List<SqlArg> args) throws SQLException {
        if (args == null || args.isEmpty()) {
            return;
        }

        for (int i = 0; i < args.size(); i++) {
            args.get(i).bind(ps, i + 1);
        }
    }
}