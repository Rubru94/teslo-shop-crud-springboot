package com.teslo.teslo_shop.core.utils;

public class MathUtil {

    /**
     * @see private constructor to prevent instantiation
     */
    private MathUtil() {

    }

    /**
     * Generate random Integer with max value
     * 
     * @param max
     * 
     * @return {@code Integer}
     */
    public static Integer getRandomInteger(Integer max) {
        return Double.valueOf(Math.floor(Math.random() * max)).intValue();
    }
}
