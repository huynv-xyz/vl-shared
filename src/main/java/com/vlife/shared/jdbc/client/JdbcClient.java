package com.vlife.shared.jdbc.client;

import com.vlife.shared.jdbc.mapper.RowMapper;
import io.micronaut.core.util.clhm.ConcurrentLinkedHashMap;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import jakarta.inject.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class JdbcClient {

    private final JdbcOperations jdbc;
    private final TxRunner tx;

    private static final Pattern NAMED = Pattern.compile("(?<!:):(\\w+)");

    private static final int MAX_CACHE_SIZE = 1000;
    private final Map<String, ParsedSql> cache = new ConcurrentLinkedHashMap.Builder<String, ParsedSql>()
            .maximumWeightedCapacity(MAX_CACHE_SIZE)
            .build();

    public JdbcClient(JdbcOperations jdbc, TxRunner tx) {
        this.jdbc = jdbc;
        this.tx = tx;
    }

    public int update(String sql, Map<String, ?> params) {
        return tx.required(() -> {
            ParsedSql ps = parse(sql, params);
            return jdbc.prepareStatement(ps.sql, st -> {
                bind(st, ps, params);
                return st.executeUpdate();
            });
        });
    }

    public int[] batchUpdate(String sql, List<? extends Map<String, ?>> batchParams) {
        if (batchParams == null || batchParams.isEmpty()) {
            return new int[0];
        }

        return tx.required(() -> {

            ParsedSql ps = parse(sql, batchParams.get(0));

            return jdbc.execute(connection -> {
                try (PreparedStatement st = connection.prepareStatement(ps.sql)) {

                    for (Map<String, ?> params : batchParams) {
                        st.clearParameters();
                        bind(st, ps, params);
                        st.addBatch();
                    }

                    return st.executeBatch();
                }
            });
        });
    }

    public <T> Optional<T> queryOne(String sql, Map<String, ?> params, RowMapper<T> mapper) {
        return tx.required(() -> {
            List<T> list = queryListInternal(sql, params, mapper, 1);
            return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
        });
    }

    public <T> List<T> queryList(String sql, Map<String, ?> params, RowMapper<T> mapper) {
        return tx.required(() -> queryListInternal(sql, params, mapper, null));
    }

    public long queryLong(String sql, Map<String, ?> params) {
        return tx.required(() -> queryOne(sql, params, rs -> rs.getLong(1)).orElse(0L));
    }

    public <T> T insertReturningId(String sql, Map<String, ?> params, Class<T> type) {
        return tx.required(() -> {
            ParsedSql ps = parse(sql, params);

            return jdbc.execute(connection -> {
                try (PreparedStatement st =
                             connection.prepareStatement(ps.sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    bind(st, ps, params);
                    st.executeUpdate();

                    try (ResultSet rs = st.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No generated key returned");
                        }
                        return cast(rs.getObject(1), type);
                    }
                }
            });
        });
    }

    private <T> List<T> queryListInternal(String sql, Map<String, ?> params, RowMapper<T> mapper, Integer maxRows) {
        ParsedSql ps = parse(sql, params);
        return jdbc.prepareStatement(ps.sql, st -> {
            if (maxRows != null) {
                st.setMaxRows(maxRows);
            }
            bind(st, ps, params);
            try (ResultSet rs = st.executeQuery()) {
                List<T> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapper.map(rs));
                }
                return out;
            }
        });
    }

    private ParsedSql parse(String sql, Map<String, ?> params) {
        boolean hasCollection = false;
        for (Object v : params.values()) {
            if (v instanceof Collection<?>) {
                hasCollection = true;
                break;
            }
        }

        if (hasCollection) {
            return parseSql(sql, params);
        }
        return cache.computeIfAbsent(sql, s -> parseSql(s, params));
    }

    private ParsedSql parseSql(String sql, Map<String, ?> params) {
        Matcher m = NAMED.matcher(sql);
        StringBuilder sb = new StringBuilder(sql.length() + 16);
        List<String> slots = new ArrayList<>();

        int last = 0;
        while (m.find()) {
            sb.append(sql, last, m.start());

            String name = m.group(1);
            if (!params.containsKey(name)) {
                throw new IllegalArgumentException("Missing value for parameter: :" + name);
            }

            Object val = params.get(name);

            if (val instanceof Collection<?> coll) {
                if (coll.isEmpty()) {
                    throw new IllegalArgumentException("Empty collection for parameter: :" + name);
                } else {
                    sb.append(coll.stream().map(x -> "?").collect(Collectors.joining(",")));
                    for (int i = 0; i < coll.size(); i++) {
                        slots.add(name);
                    }
                }
            } else {
                sb.append("?");
                slots.add(name);
            }

            last = m.end();
        }
        sb.append(sql, last, sql.length());

        return new ParsedSql(sb.toString(), slots);
    }

    private void bind(PreparedStatement st, ParsedSql ps, Map<String, ?> params) throws SQLException {
        Map<String, Iterator<?>> iterators = new HashMap<>();

        for (int i = 0; i < ps.slots.size(); i++) {
            String name = ps.slots.get(i);
            Object val = params.get(name);

            if (val instanceof Collection<?> coll) {
                Iterator<?> it = iterators.computeIfAbsent(name, k -> coll.iterator());
                st.setObject(i + 1, it.hasNext() ? it.next() : null);
            } else {
                st.setObject(i + 1, val);
            }
        }
    }

    private static <T> T cast(Object v, Class<T> type) {
        if (v == null) return null;
        if (type.isInstance(v)) return type.cast(v);

        if (v instanceof Number n) {
            if (type == Long.class) return type.cast(n.longValue());
            if (type == Integer.class) return type.cast(n.intValue());
            if (type == Short.class) return type.cast(n.shortValue());
        }

        throw new IllegalArgumentException("Cannot cast " + v.getClass().getName() + " to " + type.getName());
    }

    private record ParsedSql(String sql, List<String> slots) {
    }
}