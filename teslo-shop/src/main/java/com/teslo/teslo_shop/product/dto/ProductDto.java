package com.teslo.teslo_shop.product.dto;

import java.math.BigDecimal;
import java.util.List;

import com.teslo.teslo_shop.product.Product;

public class ProductDto {

    private String id;

    private String title;
    /*
     * private BigDecimal price;
     * private String description;
     * private String slug;
     * private Integer stock;
     * private List<String> sizes;
     * private String gender;
     * private List<String> tags;
     */

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
