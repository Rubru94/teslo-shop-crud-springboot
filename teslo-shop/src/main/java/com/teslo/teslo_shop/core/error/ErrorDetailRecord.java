package com.teslo.teslo_shop.core.error;

import java.util.Date;

public record ErrorDetailRecord(Date timestamp, String message, String details) {

    /**
     * @info
     * 
     *       Default constructor is created automatically if defined as record.
     */
    public ErrorDetailRecord(String message, String details) {
        this(new Date(), message, details);
    }
}
