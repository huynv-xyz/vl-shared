package com.vlife.shared.jdbc.util;

public final class NameUtil {
    private NameUtil() {
    }

    /**
     * content_id -> contentId
     */
    public static String snakeToCamel(String s) {
        if (s == null || s.isBlank()) return s;
        StringBuilder out = new StringBuilder(s.length());
        boolean up = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                up = true;
                continue;
            }
            if (up) {
                out.append(Character.toUpperCase(c));
                up = false;
            } else out.append(c);
        }
        return out.toString();
    }
}
