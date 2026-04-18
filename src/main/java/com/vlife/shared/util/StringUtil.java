package com.vlife.shared.util;

public final class StringUtil {

    private StringUtil() {}

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
}