package com.vlife.shared.jdbc.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SqlBuilder {

    private final StringBuilder where = new StringBuilder(" WHERE 1=1 ");
    private final Map<String, Object> params = new LinkedHashMap<>();
    private String orderBy = "";
    private Integer limit = null;
    private Long offset = null;

    public static SqlBuilder where() {
        return new SqlBuilder();
    }

    public SqlBuilder orderBy(String expr) {
        if (expr != null && !expr.isBlank()) {
            this.orderBy = " ORDER BY " + expr;
        }
        return this;
    }

    public SqlBuilder limit(int limit) {
        this.limit = limit;
        this.params.put("_limit", limit);
        return this;
    }

    public SqlBuilder offset(long offset) {
        this.offset = offset;
        this.params.put("_offset", offset);
        return this;
    }

    public SqlBuilder append(String sqlFragment, Map<String, ?> extraParams) {
        if (sqlFragment == null || sqlFragment.isBlank()) {
            return this;
        }
        where.append(sqlFragment.startsWith(" ") ? "" : " ").append(sqlFragment);
        if (extraParams != null && !extraParams.isEmpty()) {
            params.putAll(extraParams);
        }
        return this;
    }

    public SqlBuilder raw(String sqlFragment) {
        if (sqlFragment == null || sqlFragment.isBlank()) {
            return this;
        }

        String trimmed = sqlFragment.trim().toUpperCase();
        if (!trimmed.startsWith("AND") && !trimmed.startsWith("OR")) {
            where.append(" AND ");
        }
        where.append(" ").append(sqlFragment).append(" ");
        return this;
    }

    public SqlBuilder param(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public SqlBuilder eq(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" = :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder ne(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" <> :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder gt(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" > :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder gte(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" >= :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder lt(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" < :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder lte(String expr, String param, Object val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" <= :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder likeContains(String expr, String param, String val) {
        if (val == null || val.isBlank()) {
            return this;
        }
        where.append(" AND ").append(expr)
                .append(" LIKE CONCAT('%', :").append(param).append(", '%') ");
        params.put(param, val.trim());
        return this;
    }

    public SqlBuilder likeStartsWith(String expr, String param, String val) {
        if (val == null || val.isBlank()) {
            return this;
        }
        where.append(" AND ").append(expr)
                .append(" LIKE CONCAT(:").append(param).append(", '%') ");
        params.put(param, val.trim());
        return this;
    }

    public SqlBuilder likeEndsWith(String expr, String param, String val) {
        if (val == null || val.isBlank()) {
            return this;
        }
        where.append(" AND ").append(expr)
                .append(" LIKE CONCAT('%', :").append(param).append(") ");
        params.put(param, val.trim());
        return this;
    }

    public SqlBuilder bool(String expr, String param, Boolean val) {
        if (val == null) {
            return this;
        }
        where.append(" AND ").append(expr).append(" = :").append(param).append(" ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder isNull(String expr) {
        if (expr == null || expr.isBlank()) {
            return this;
        }
        where.append(" AND ").append(expr).append(" IS NULL ");
        return this;
    }

    public SqlBuilder isNotNull(String expr) {
        if (expr == null || expr.isBlank()) {
            return this;
        }
        where.append(" AND ").append(expr).append(" IS NOT NULL ");
        return this;
    }

    public SqlBuilder in(String expr, String param, Object val) {
        if (isEmpty(val)) {
            return this;
        }
        where.append(" AND ").append(expr).append(" IN (:").append(param).append(") ");
        params.put(param, val);
        return this;
    }

    public SqlBuilder between(String expr, String fromParam, Object fromVal, String toParam, Object toVal) {
        if (fromVal != null) {
            gte(expr, fromParam, fromVal);
        }
        if (toVal != null) {
            lte(expr, toParam, toVal);
        }
        return this;
    }

    private boolean isEmpty(Object val) {
        if (val == null) return true;
        if (val instanceof Collection<?> c) return c.isEmpty();
        if (val.getClass().isArray()) return Array.getLength(val) == 0;
        return false;
    }

    public String sql() {
        StringBuilder sb = new StringBuilder(where);
        sb.append(orderBy);
        if (limit != null) {
            sb.append(" LIMIT :_limit");
        }
        if (offset != null) {
            sb.append(" OFFSET :_offset");
        }
        return sb.toString();
    }

    public Map<String, Object> params() {
        return params;
    }
}