package com.amido.stacks.core.mapping;

import static java.util.UUID.fromString;

import java.util.UUID;

public class MapperUtils {

    private MapperUtils() {
        // Utility class
    }

    public static UUID map(String value) {
        return (value != null && !value.trim().isEmpty()) ? fromString(value) : null;
    }

    public static String map(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}
