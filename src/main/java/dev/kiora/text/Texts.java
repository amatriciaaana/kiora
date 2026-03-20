package dev.kiora.text;

import java.util.List;

/**
 * Small string utilities for null-safe text handling.
 */
public final class Texts {
    private Texts() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String nullIfBlank(String value) {
        return isBlank(value) ? null : value;
    }

    public static String emptyToNull(String value) {
        return isEmpty(value) ? null : value;
    }

    public static String blankToDefault(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    public static List<String> lines(String value) {
        if (value == null || value.isEmpty()) {
            return List.of();
        }
        return value.lines().toList();
    }
}
