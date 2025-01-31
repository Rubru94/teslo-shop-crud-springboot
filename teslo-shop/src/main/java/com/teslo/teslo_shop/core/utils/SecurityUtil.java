package com.teslo.teslo_shop.core.utils;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * @see private constructor to prevent instantiation
     */
    private SecurityUtil() {

    }

    /**
     * Get authentication principal object
     * 
     * @return {@code Object}
     */
    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Check if String params roles matches user's role
     * 
     * @param roles
     * @return {@code true} if {@code role} match, {@code false} if not
     */
    public static boolean hasAnyRole(List<String> roles) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roles::contains);
    }
}
