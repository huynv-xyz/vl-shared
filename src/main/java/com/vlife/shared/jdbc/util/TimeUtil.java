package com.vlife.shared.jdbc.util;

import java.time.*;

public final class TimeUtil {
    private TimeUtil() {
    }

    /**
     * Mặc định UTC cho nhất quán DB.
     * Nếu anh muốn local time thì đổi ZoneOffset.UTC -> ZoneId.systemDefault()
     */
    public static Object nowForType(Class<?> type) {
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);

        if (type == OffsetDateTime.class) return odt;
        if (type == Instant.class) return odt.toInstant();
        if (type == LocalDateTime.class) return odt.toLocalDateTime();
        if (type == Long.class || type == long.class) return odt.toInstant().toEpochMilli(); // epoch millis
        if (type == Integer.class || type == int.class) return (int) odt.toInstant().getEpochSecond(); // epoch sec
        return odt;
    }
}
