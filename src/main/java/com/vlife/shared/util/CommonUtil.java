package com.vlife.shared.util;

import java.util.Collection;
import java.util.Map;

public class CommonUtil {
    public static boolean isNullOrEmpty(String input) {
        return isNullOrEmpty(input, true);
    }

    public static boolean isNullOrEmpty(String input, boolean isTrim) {
        return input == null || (isTrim ? input.trim().isEmpty() : input.isEmpty());
    }

    public static <T> boolean isNullOrEmpty(Collection<T> input) {
        return input == null || input.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> input) {
        return input == null || input.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(T[] input) {
        return input == null || input.length == 0;
    }
}
