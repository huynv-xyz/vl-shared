package com.vlife.shared.jdbc.builder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

public sealed interface SqlArg permits SqlArg.Value {

    void bind(PreparedStatement ps, int index) throws SQLException;

    static SqlArg val(Object v) {
        return new Value(v);
    }

    record Value(Object v) implements SqlArg {
        @Override
        public void bind(PreparedStatement ps, int index) throws SQLException {
            if (v == null) {
                ps.setNull(index, Types.NULL);
            } else if (v instanceof String value) {
                ps.setString(index, value);
            } else if (v instanceof Integer value) {
                ps.setInt(index, value);
            } else if (v instanceof Long value) {
                ps.setLong(index, value);
            } else if (v instanceof Short value) {
                ps.setShort(index, value);
            } else if (v instanceof Double value) {
                ps.setDouble(index, value);
            } else if (v instanceof Float value) {
                ps.setFloat(index, value);
            } else if (v instanceof Boolean value) {
                ps.setBoolean(index, value);
            } else if (v instanceof LocalDate value) {
                ps.setObject(index, value);
            } else if (v instanceof LocalDateTime value) {
                ps.setObject(index, value);
            } else if (v instanceof Enum<?> value) {
                ps.setString(index, value.name());
            } else {
                ps.setObject(index, v);
            }
        }
    }
}