package com.teslo.teslo_shop.auth.enums;

import org.springframework.security.core.GrantedAuthority;

public enum ValidRoles implements GrantedAuthority {
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

    @Override
    public String getAuthority() {
        return str;
    }
}
