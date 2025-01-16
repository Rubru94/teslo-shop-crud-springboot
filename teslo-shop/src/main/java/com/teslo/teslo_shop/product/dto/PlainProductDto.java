package com.teslo.teslo_shop.product.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

public class PlainProductDto {

    private String id;
    private String title;
    private BigDecimal price;
    private String description;
    private String slug;
    private Integer stock;
    private List<String> sizes;
    private String gender;
    private List<String> tags;
    private String type;
    private List<String> images;

    /**
     * @see
     *      · Jackson needs a default constructor for instantiating the class
     */
    public PlainProductDto() {
        this.stock = 0;
        this.images = new ArrayList<>();
    }

    public PlainProductDto(ProductDto productDto) {
        this.id = productDto.getId();
        this.title = productDto.getTitle();
        this.price = productDto.getPrice();
        this.description = productDto.getDescription();
        this.slug = productDto.getSlug();
        this.stock = productDto.getStock();
        this.sizes = productDto.getSizes();
        this.gender = productDto.getGender();
        this.tags = productDto.getTags();
        this.type = productDto.getType();
        this.images = productDto.getImages().stream().map(ProductImageDto::getUrl).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
