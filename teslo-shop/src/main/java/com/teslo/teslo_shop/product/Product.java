package com.teslo.teslo_shop.product;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    /*
     * @info
     * 
     * Precision is the number of digits in a number.
     * Scale is the number of digits to the right of the decimal point in a number
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer stock = 0;

    private List<String> sizes;

    private String gender;

    private List<String> tags;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }

    public Integer getStock() {
        return stock;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getTags() {
        return tags;
    }
}
