package com.teslo.teslo_shop.auth.dto;

import java.util.List;

import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.auth.interfaces.UserInterface;
import com.teslo.teslo_shop.product.Product;

public class AuthenticatedUserDto implements UserInterface {

    private User user;
    private String token;
    private Long expiresIn;

    public AuthenticatedUserDto() {
        this.user = new User();
    }

    public AuthenticatedUserDto(User user) {
        this.user = user;
    }

    public AuthenticatedUserDto(User user, String token) {
        this(user);
        this.token = token;
    }

    public AuthenticatedUserDto(User user, String token, Long expiresIn) {
        this(user, token);
        this.expiresIn = expiresIn;
    }

    public AuthenticatedUserDto(CreateUserDto createUserDto) {
        this();
        this.setEmail(createUserDto.getEmail());
        this.setFullName(createUserDto.getFullName());
    }

    public AuthenticatedUserDto(CreateUserDto createUserDto, String token) {
        this(createUserDto);
        this.token = token;
    }

    public String getId() {
        return user.getId();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public String getFullName() {
        return user.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        user.setFullName(fullName);
    }

    @Override
    public Boolean isActive() {
        return user.isActive();
    }

    @Override
    public void setActive(Boolean isActive) {
        user.setActive(isActive);
    }

    @Override
    public List<String> getRoles() {
        return user.getRoles();
    }

    @Override
    public void setRoles(List<String> roles) {
        user.setRoles(roles);
    }

    @Override
    public List<Product> getProducts() {
        return user.getProducts();
    }

    @Override
    public void setProducts(List<Product> products) {
        user.setProducts(products);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
