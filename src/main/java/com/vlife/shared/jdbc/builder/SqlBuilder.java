package com.vlife.shared.jdbc.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class SqlBuilder {

    private final StringBuilder where = new StringBuilder(" WHERE 1=1 ");
    private final List<SqlArg> args = new ArrayList<>();

    public SqlBuilder eq(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" = ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder ne(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" <> ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder gte(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" >= ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder gt(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" > ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder lte(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" <= ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder lt(String col, Object v) {
        if (v == null) return this;
        where.append(" AND ").append(col).append(" < ? ");
        args.add(SqlArg.val(v));
        return this;
    }

    public SqlBuilder like(String col, String v) {
        if (v == null || v.isBlank()) return this;
        where.append(" AND ").append(col).append(" LIKE ? ");
        args.add(SqlArg.val("%" + v.trim() + "%"));
        return this;
    }

    public SqlBuilder in(String col, List<?> values) {
        if (values == null || values.isEmpty()) return this;

        where.append(" AND ").append(col).append(" IN (");
        StringJoiner joiner = new StringJoiner(", ");
        for (Object value : values) {
            joiner.add("?");
            args.add(SqlArg.val(value));
        }
        where.append(joiner).append(") ");
        return this;
    }

    public SqlBuilder inShort(String col, Short[] values) {
        if (values == null || values.length == 0) return this;

        where.append(" AND ").append(col).append(" IN (");
        StringJoiner joiner = new StringJoiner(", ");
        for (Short value : values) {
            joiner.add("?");
            args.add(SqlArg.val(value));
        }
        where.append(joiner).append(") ");
        return this;
    }

    public SqlBuilder raw(String clause, SqlArg... rawArgs) {
        if (clause == null || clause.isBlank()) return this;

        where.append(" AND ").append(clause).append(" ");
        if (rawArgs != null) {
            for (SqlArg arg : rawArgs) {
                args.add(arg);
            }
        }
        return this;
    }

    public String whereSql() {
        return where.toString();
    }

    public List<SqlArg> args() {
        return args;
    }
}