package com.vlife.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberUtil {

    private NumberUtil() {
    }

    public static double nvl(Double v) {
        return v == null ? 0D : v;
    }

    public static int nvl(Integer v) {
        return v == null ? 0 : v;
    }

    public static long nvl(Long v) {
        return v == null ? 0L : v;
    }

    public static double round2(double v) {
        return BigDecimal.valueOf(v)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static double round4(double v) {
        return BigDecimal.valueOf(v)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static double percent(double numerator, double denominator) {
        if (denominator == 0D) {
            return 0D;
        }
        return numerator / denominator;
    }

    public static boolean isZero(Double v) {
        return nvl(v) == 0D;
    }

    public static boolean gtZero(Double v) {
        return nvl(v) > 0D;
    }

    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    public static double min(double a, double b) {
        return Math.min(a, b);
    }


    public static Integer parseInt(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static Double parseDouble(String value) {
        try {
            return value == null || value.isBlank() ? null : Double.parseDouble(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseString(Object value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}