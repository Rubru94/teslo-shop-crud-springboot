package com.teslo.teslo_shop.auth.dto;

import com.teslo.teslo_shop.auth.entities.User;

public class LoginUserDto {

    private User user;

    public LoginUserDto() {
        this.user = new User();
    }

    public LoginUserDto(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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
}
