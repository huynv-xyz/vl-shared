package com.vlife.shared.jdbc.metadata;

import com.vlife.shared.jdbc.util.NameUtil;
import io.micronaut.data.annotation.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityIntrospector {

    private EntityIntrospector() {
    }

    private static final Map<Class<?>, EntityMeta> CACHE = new ConcurrentHashMap<>();

    public static <T> EntityMeta meta(Class<T> entityClass) {
        return CACHE.computeIfAbsent(entityClass, EntityIntrospector::inspect);
    }

    private static EntityMeta inspect(Class<?> cls) {
        MappedEntity me = cls.getAnnotation(MappedEntity.class);
        if (me == null || me.value() == null || me.value().isBlank()) {
            throw new IllegalArgumentException("Missing @MappedEntity(value=...) on " + cls.getName());
        }
        String table = me.value().trim();

        List<ColumnField> cols = new ArrayList<>();
        ColumnField idField = null;

        for (Field f : allFields(cls)) {
            if (f.getAnnotation(Transient.class) != null) continue;

            boolean isId = f.getAnnotation(Id.class) != null;

            MappedProperty mp = f.getAnnotation(MappedProperty.class);
            String column = (mp != null && mp.value() != null && !mp.value().isBlank())
                    ? mp.value().trim()
                    : f.getName();

            String paramName = NameUtil.snakeToCamel(column);

            boolean generated = f.getAnnotation(GeneratedValue.class) != null;
            boolean dateCreated = f.getAnnotation(DateCreated.class) != null;
            boolean dateUpdated = f.getAnnotation(DateUpdated.class) != null;

            ColumnField cf = new ColumnField(f, column, paramName, isId, generated, dateCreated, dateUpdated);

            if (isId) idField = cf;
            else cols.add(cf);
        }

        if (idField == null) {
            throw new IllegalArgumentException("No @Id field found in " + cls.getName());
        }

        return new EntityMeta(table, idField, cols);
    }

    private static List<Field> allFields(Class<?> cls) {
        List<Field> out = new ArrayList<>();
        Class<?> c = cls;
        while (c != null && c != Object.class) {
            out.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return out;
    }
}
