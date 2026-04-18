package com.vlife.shared.jdbc.dao.base;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.mapper.EntityMappers;
import com.vlife.shared.jdbc.mapper.RowMapper;
import com.vlife.shared.jdbc.metadata.ColumnField;
import com.vlife.shared.jdbc.metadata.EntityIntrospector;
import com.vlife.shared.jdbc.metadata.EntityMeta;
import com.vlife.shared.jdbc.util.SqlBuilder;
import com.vlife.shared.util.CommonUtil;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public abstract class BaseDao<T, ID> {

    protected final JdbcClient jdbc;
    protected final Class<T> entityClass;
    protected final EntityMeta meta;
    protected final RowMapper<T> mapper;

    private final Class<ID> idClass;
    private final Field idField;

    private final String sqlBaseSelect;
    private final String sqlFindById;
    private final String sqlDeleteById;
    private final String sqlCountAll;

    private final String cachedInsertCols;
    private final String cachedInsertValues;

    @SuppressWarnings("unchecked")
    protected BaseDao(JdbcClient jdbc, Class<T> entityClass) {
        this.jdbc = jdbc;
        this.entityClass = entityClass;
        this.meta = EntityIntrospector.meta(entityClass);
        this.mapper = EntityMappers.rowMapper(entityClass);

        this.idClass = (Class<ID>) ((java.lang.reflect.ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[1];

        try {
            String fieldName = meta.id().paramName();
            this.idField = entityClass.getDeclaredField(fieldName);
            this.idField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Không tìm thấy field ID: " + meta.id().paramName(), e);
        }

        StringJoiner selectCols = new StringJoiner(", ");
        selectCols.add(meta.id().column());
        for (ColumnField c : meta.columns()) {
            selectCols.add(c.column());
        }

        this.sqlBaseSelect = "SELECT " + selectCols + " FROM " + meta.table();
        this.sqlFindById = this.sqlBaseSelect + " WHERE " + meta.id().column() + " = :id";
        this.sqlDeleteById = "DELETE FROM " + meta.table() + " WHERE " + meta.id().column() + " = :id";
        this.sqlCountAll = "SELECT COUNT(*) FROM " + meta.table();

        StringJoiner insertCols = new StringJoiner(", ");
        StringJoiner insertVals = new StringJoiner(", ");
        for (ColumnField c : meta.columns()) {
            insertCols.add(c.column());
            insertVals.add(":" + c.paramName());
        }
        this.cachedInsertCols = insertCols.toString();
        this.cachedInsertValues = insertVals.toString();
    }

    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        return jdbc.queryOne(sqlFindById, Map.of("id", id), mapper);
    }

    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }

        Long cnt = jdbc.queryLong(
                "SELECT COUNT(*) FROM " + meta.table() + " WHERE " + meta.id().column() + " = :id",
                Map.of("id", id)
        );
        return cnt != null && cnt > 0;
    }

    public List<T> findByIds(Collection<ID> ids) {
        if (CommonUtil.isNullOrEmpty(ids)) {
            return new ArrayList<>();
        }

        SqlBuilder sb = SqlBuilder.where()
                .in(meta.id().column(), "ids", ids);

        return queryList(sb);
    }

    public Map<ID, T> findByIdsAsMap(Collection<ID> ids) {
        List<T> list = findByIds(ids);
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<ID, T> out = new LinkedHashMap<>(list.size());
        for (T entity : list) {
            ID id = getEntityId(entity);
            if (id != null) {
                out.put(id, entity);
            }
        }
        return out;
    }

    public int deleteById(ID id) {
        if (id == null) {
            return 0;
        }
        return jdbc.update(sqlDeleteById, Map.of("id", id));
    }

    public long countAll() {
        return jdbc.queryLong(sqlCountAll, Collections.emptyMap());
    }

    public T insert(T entity) {
        EntityMappers.autoFillOnInsert(entity);
        Map<String, Object> params = EntityMappers.toParams(entity, false, false);

        String sql = "INSERT INTO " + meta.table()
                + " (" + cachedInsertCols + ") VALUES (" + cachedInsertValues + ")";

        ID id = jdbc.insertReturningId(sql, params, idClass);
        setEntityId(entity, id);
        return entity;
    }

    public int insertSelective(T entity) {
        EntityMappers.autoFillOnInsert(entity);
        Map<String, Object> params = EntityMappers.toParams(entity, false, false);

        StringJoiner cols = new StringJoiner(", ");
        StringJoiner vals = new StringJoiner(", ");

        for (ColumnField c : meta.columns()) {
            Object value = params.get(c.paramName());
            if (value != null) {
                cols.add(c.column());
                vals.add(":" + c.paramName());
            }
        }

        if (cols.length() == 0) {
            return 0;
        }

        String sql = "INSERT INTO " + meta.table()
                + " (" + cols + ") VALUES (" + vals + ")";

        return jdbc.update(sql, params);
    }

    public int[] saveAll(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return new int[0];
        }

        String sql = "INSERT INTO " + meta.table()
                + " (" + cachedInsertCols + ") VALUES (" + cachedInsertValues + ")";

        List<Map<String, Object>> batchParams = new ArrayList<>();

        for (T entity : entities) {
            EntityMappers.autoFillOnInsert(entity);
            Map<String, Object> params = EntityMappers.toParams(entity, false, false);
            batchParams.add(params);
        }

        return jdbc.batchUpdate(sql, batchParams);
    }

    /**
     * Upsert theo primary key cho MySQL.
     * Dùng INSERT ... ON DUPLICATE KEY UPDATE
     */
    public int upsertByPk(T entity) {
        Map<String, Object> params = EntityMappers.toParams(entity, true, false);

        StringJoiner cols = new StringJoiner(", ");
        StringJoiner vals = new StringJoiner(", ");

        cols.add(meta.id().column());
        vals.add(":" + meta.id().paramName());

        for (ColumnField c : meta.columns()) {
            cols.add(c.column());
            vals.add(":" + c.paramName());
        }

        StringJoiner sets = new StringJoiner(", ");
        for (ColumnField c : meta.columns()) {
            sets.add(c.column() + " = VALUES(" + c.column() + ")");
        }

        String sql = "INSERT INTO " + meta.table()
                + " (" + cols + ") VALUES (" + vals + ") "
                + "ON DUPLICATE KEY UPDATE " + sets;

        return jdbc.update(sql, params);
    }

    public int updateSelective(ID id, T entity) {
        if (id == null) {
            return 0;
        }

        EntityMappers.autoFillOnUpdate(entity);
        Map<String, Object> params = new LinkedHashMap<>(EntityMappers.toParams(entity, false, true));

        if (params.isEmpty()) {
            return 0;
        }

        params.put("id", id);

        StringJoiner sets = new StringJoiner(", ");
        for (ColumnField c : meta.columns()) {
            if (params.containsKey(c.paramName())) {
                sets.add(c.column() + " = :" + c.paramName());
            }
        }

        String sql = "UPDATE " + meta.table()
                + " SET " + sets
                + " WHERE " + meta.id().column() + " = :id";

        return jdbc.update(sql, params);
    }

    /**
     * Full update: update toàn bộ cột trừ id.
     */
    public int updateAll(ID id, T entity) {
        if (id == null) {
            return 0;
        }

        EntityMappers.autoFillOnUpdate(entity);
        Map<String, Object> params = new LinkedHashMap<>(EntityMappers.toParams(entity, false, false));
        params.put("id", id);

        StringJoiner sets = new StringJoiner(", ");
        for (ColumnField c : meta.columns()) {
            sets.add(c.column() + " = :" + c.paramName());
        }

        String sql = "UPDATE " + meta.table()
                + " SET " + sets
                + " WHERE " + meta.id().column() + " = :id";

        return jdbc.update(sql, params);
    }

    public Optional<T> queryOne(SqlBuilder sb) {
        return jdbc.queryOne(sqlBaseSelect + sb.sql() + " LIMIT 1", sb.params(), mapper);
    }

    public List<T> queryList(SqlBuilder sb) {
        return jdbc.queryList(sqlBaseSelect + sb.sql(), sb.params(), mapper);
    }

    public Page<T> queryPage(SqlBuilder sb, Pageable pageable, String defaultOrderBy) {
        String whereSql = sb.sql();
        long total = jdbc.queryLong(
                "SELECT COUNT(*) FROM " + meta.table() + whereSql,
                sb.params()
        );

        if (total == 0) {
            return Page.empty();
        }

        sb.orderBy(defaultOrderBy)
                .limit(pageable.getSize())
                .offset(pageable.getOffset());

        List<T> items = jdbc.queryList(sqlBaseSelect + sb.sql(), sb.params(), mapper);
        return Page.of(items, pageable, total);
    }

    public Page<T> findAll(Pageable pageable) {
        SqlBuilder sb = SqlBuilder.where();
        return queryPage(sb, pageable, meta.id().column() + " DESC");
    }

    protected String baseSelectSql() {
        return sqlBaseSelect;
    }

    protected Optional<Integer> queryIntOpt(String sql, Map<String, ?> params) {
        return jdbc.queryOne(sql, params, rs -> rs.getInt(1));
    }

    protected Integer queryInt(String sql, Map<String, ?> params, Integer defaultVal) {
        return queryIntOpt(sql, params).orElse(defaultVal);
    }

    protected Long queryLong(String sql, Map<String, ?> params) {
        return jdbc.queryLong(sql, params);
    }

    public List<T> queryList(String sql, Map<String, ?> params) {
        return jdbc.queryList(sql, params, mapper);
    }

    @SuppressWarnings("unchecked")
    protected ID getEntityId(T entity) {
        try {
            return (ID) idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error extracting ID from " + entityClass.getSimpleName(), e);
        }
    }

    protected void setEntityId(T entity, ID id) {
        try {
            idField.set(entity, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Không set được ID vào entity " + entityClass.getSimpleName(), e);
        }
    }

    /**
     * Hữu ích khi cần insert/update custom với subset cột.
     */
    protected Map<String, Object> filterParams(Map<String, Object> source, String... allowedKeys) {
        if (source == null || source.isEmpty() || allowedKeys == null || allowedKeys.length == 0) {
            return Collections.emptyMap();
        }

        LinkedHashSet<String> allowed = new LinkedHashSet<>(List.of(allowedKeys));
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : source.entrySet()) {
            if (allowed.contains(e.getKey())) {
                out.put(e.getKey(), e.getValue());
            }
        }
        return out;
    }
}