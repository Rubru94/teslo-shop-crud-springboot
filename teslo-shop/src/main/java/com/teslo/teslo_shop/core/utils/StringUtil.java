package com.teslo.teslo_shop.core.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * @see private constructor to prevent instantiation
     */
    private StringUtil() {

    }

    /**
     * Check if string is null or empty
     * 
     * @param str
     * 
     * @return {@code true} if {@code str} is null or empty, {@code false} if not
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if param {@code str} is a valid UUID
     * 
     * @param str
     * 
     * @return {@code true} if {@code str} is valid UUID, {@code false} if not
     */
    public static boolean isUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
