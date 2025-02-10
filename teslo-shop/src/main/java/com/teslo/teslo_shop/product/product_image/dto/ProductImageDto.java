package com.teslo.teslo_shop.product.product_image.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductImageDto that = (ProductImageDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(url, that.url) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, productId);
    }
}
