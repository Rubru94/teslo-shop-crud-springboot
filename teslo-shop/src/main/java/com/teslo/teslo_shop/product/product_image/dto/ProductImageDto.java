package com.teslo.teslo_shop.product.product_image.dto;

import com.teslo.teslo_shop.product.product_image.ProductImage;

public class ProductImageDto {

    private Long id;
    private String url;
    private String productId;

    public ProductImageDto() {
    }

    public ProductImageDto(ProductImage image) {
        this.id = image.getId();
        this.url = image.getUrl();
        this.productId = image.getProduct().getId();
    }

    public ProductImageDto(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
