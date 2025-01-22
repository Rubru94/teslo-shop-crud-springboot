package com.teslo.teslo_shop.auth.enums;

public enum ValidRoles {
    ADMIN("admin"),
    USER("user"),
    SUPER_USER("super-user"),
    ;

    private String str;

    ValidRoles(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
