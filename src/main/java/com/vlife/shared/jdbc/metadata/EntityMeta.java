package com.vlife.shared.jdbc.metadata;

import java.util.List;

/**
 * Metadata của Entity, được build từ Micronaut Data annotations.
 * <p>
 * - table        : tên bảng (snake_case)
 * - id           : field @Id
 * - columns      : các field non-id (snake_case column, camelCase param)
 */
public record EntityMeta(
        String table,
        ColumnField id,
        List<ColumnField> columns
) {
}
