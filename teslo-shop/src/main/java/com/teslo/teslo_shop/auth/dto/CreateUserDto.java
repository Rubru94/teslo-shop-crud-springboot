package com.teslo.teslo_shop.auth.dto;

import com.teslo.teslo_shop.auth.entities.User;

public class CreateUserDto {

    private User user;

    public CreateUserDto() {
        this.user = new User();
    }

    public CreateUserDto(User user) {
        this.user = user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public String getFullName() {
        return user.getFullName();
    }

    public void setFullName(String fullName) {
        user.setFullName(fullName);
    }
}
