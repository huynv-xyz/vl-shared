package com.vlife.shared.jdbc.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlife.shared.jdbc.metadata.ColumnField;
import com.vlife.shared.jdbc.metadata.EntityIntrospector;
import com.vlife.shared.jdbc.metadata.EntityMeta;
import com.vlife.shared.jdbc.util.TimeUtil;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityMappers {

    private EntityMappers() {
    }

    private static final Map<Class<?>, RowMapper<?>> ROW_MAPPER_CACHE = new ConcurrentHashMap<>();
    private static final ObjectMapper OM = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_REF = new TypeReference<>() {
    };
    private static final TypeReference<List<Object>> LIST_REF = new TypeReference<>() {
    };

    @SuppressWarnings("unchecked")
    public static <T> RowMapper<T> rowMapper(Class<T> cls) {
        return (RowMapper<T>) ROW_MAPPER_CACHE.computeIfAbsent(cls, EntityMappers::buildMapper);
    }

    private static <T> RowMapper<T> buildMapper(Class<T> cls) {
        EntityMeta meta = EntityIntrospector.meta(cls);
        Constructor<T> ctor = noArgCtor(cls);

        List<ColumnField> all = new ArrayList<>();
        all.add(meta.id());
        all.addAll(meta.columns());

        return rs -> {
            try {
                T obj = ctor.newInstance();
                for (ColumnField cf : all) {
                    Object v = read(rs, cf.column(), cf.field().getType());
                    if (v == null) {
                        if (cf.field().getType().isPrimitive()) continue;
                    } else {
                        v = convert(v, cf.field().getType());
                    }
                    cf.field().set(obj, v);
                }
                return obj;
            } catch (Exception e) {
                throw new SQLException("Map error for " + cls.getName() + ": " + e.getMessage(), e);
            }
        };
    }

    private static Object convert(Object v, Class<?> targetType) {
        if (v == null || targetType.isInstance(v)) {
            return v;
        }

        if (v instanceof Number num) {
            if (targetType == Integer.class || targetType == int.class) {
                return num.intValue();
            } else if (targetType == Long.class || targetType == long.class) {
                return num.longValue();
            } else if (targetType == Double.class || targetType == double.class) {
                return num.doubleValue();
            } else if (targetType == Float.class || targetType == float.class) {
                return num.floatValue();
            } else if (targetType == Short.class || targetType == short.class) {
                return num.shortValue();
            } else if (targetType == Byte.class || targetType == byte.class) {
                return num.byteValue();
            }
        }

        if (v instanceof Number num && (targetType == Boolean.class || targetType == boolean.class)) {
            return num.intValue() != 0;
        }

        return v;
    }

    private static <T> Constructor<T> noArgCtor(Class<T> cls) {
        try {
            Constructor<T> c = cls.getDeclaredConstructor();
            c.setAccessible(true);
            return c;
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity must have no-arg constructor: " + cls.getName(), e);
        }
    }

    private static Object read(ResultSet rs, String col, Class<?> type) throws SQLException {
        try {
            if (type.isArray()) {
                java.sql.Array sqlArray = rs.getArray(col);
                if (sqlArray == null) return null;
                try {
                    return sqlArray.getArray();
                } finally {
                    sqlArray.free();
                }
            }

            return rs.getObject(col, type);
        } catch (Exception ignore) {
            return rs.getObject(col);
        }
    }

    /**
     * Auto fill timestamps:
     * - INSERT: @DateCreated set if null; @DateUpdated set if null
     */
    public static <T> void autoFillOnInsert(T entity) {
        Objects.requireNonNull(entity);
        EntityMeta meta = EntityIntrospector.meta(entity.getClass());

        for (ColumnField cf : meta.columns()) {
            if (cf.dateCreated() || cf.dateUpdated()) fillIfNull(entity, cf);
        }
    }

    /**
     * UPDATE: @DateUpdated ALWAYS set = now
     */
    public static <T> void autoFillOnUpdate(T entity) {
        Objects.requireNonNull(entity);
        EntityMeta meta = EntityIntrospector.meta(entity.getClass());

        for (ColumnField cf : meta.columns()) {
            if (cf.dateUpdated()) setNow(entity, cf);
        }
    }

    private static void fillIfNull(Object entity, ColumnField cf) {
        try {
            Object v = cf.field().get(entity);
            if (v != null) return;
            cf.field().set(entity, TimeUtil.nowForType(cf.field().getType()));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot autofill field: " + cf.field().getName(), e);
        }
    }

    private static void setNow(Object entity, ColumnField cf) {
        try {
            cf.field().set(entity, TimeUtil.nowForType(cf.field().getType()));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set now for field: " + cf.field().getName(), e);
        }
    }

    /**
     * entity -> params map (keys = camelCase paramName)
     * includeId: add id param or not
     * selective: only include field != null
     */
    public static <T> Map<String, Object> toParams(T entity, boolean includeId, boolean selective) {
        Objects.requireNonNull(entity, "entity");
        EntityMeta meta = EntityIntrospector.meta(entity.getClass());

        Map<String, Object> p = new LinkedHashMap<>();

        if (includeId) putField(entity, meta.id(), p, selective);
        for (ColumnField cf : meta.columns()) putField(entity, cf, p, selective);

        return p;
    }

    private static void putField(Object entity, ColumnField cf, Map<String, Object> p, boolean selective) {
        try {
            Object v = cf.field().get(entity);
            if (selective && v == null) return;
            p.put(cf.paramName(), v);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read field: " + cf.field().getName(), e);
        }
    }
}
