package com.vlife.shared.jdbc.metadata;

import java.lang.reflect.Field;

/**
 * Mapping 1 field Java <-> 1 column DB
 * <p>
 * column    : snake_case (DB)
 * paramName : camelCase (named param trong SQL)
 */
public record ColumnField(
        Field field,
        String column,      // DB column (snake_case)
        String paramName,   // SQL named param (camelCase)
        boolean id,
        boolean generated,
        boolean dateCreated,
        boolean dateUpdated
) {
    public ColumnField {
        field.setAccessible(true);
    }
}
