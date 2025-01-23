package com.teslo.teslo_shop.auth.interfaces;

import java.util.List;

import com.teslo.teslo_shop.product.Product;

public interface UserInterface {
    String getEmail();

    void setEmail(String email);

    String getFullName();

    void setFullName(String fullName);

    Boolean isActive();

    void setActive(Boolean isActive);

    List<String> getRoles();

    void setRoles(List<String> roles);

    List<Product> getProducts();

    void setProducts(List<Product> products);
}
