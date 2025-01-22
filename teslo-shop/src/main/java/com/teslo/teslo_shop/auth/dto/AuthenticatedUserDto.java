package com.teslo.teslo_shop.auth.dto;

import com.teslo.teslo_shop.auth.entities.User;

public class AuthenticatedUserDto extends User {

    private String token;

    public AuthenticatedUserDto() {
        super();
    }

    public AuthenticatedUserDto(CreateUserDto createUserDto) {
        this();
        this.setEmail(createUserDto.getEmail());
        this.setFullName(createUserDto.getFullName());
        this.setPassword(createUserDto.getPassword());
    }

    public AuthenticatedUserDto(CreateUserDto createUserDto, String token) {
        this(createUserDto);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
