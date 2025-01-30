package com.teslo.teslo_shop.product.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.teslo.teslo_shop.product.Product;
import com.teslo.teslo_shop.product.interfaces.ProductInterface;

/**
 * @see {@code abstract} ensures the class cannot be instantiated directly,
 *      as its purpose is to be extended by other classes that can add specific
 *      behaviours as needed.
 */
public abstract class BaseProductDto<T> implements ProductInterface<T> {

    /**
     * @see {@code protected} shall only be visible/usable by subclasses and other
     *      classes of the same package.
     */
    protected Product product;
    private List<T> images;

    public BaseProductDto() {
        this.product = new Product();
        this.images = new ArrayList<T>();
    }

    public BaseProductDto(Product product) {
        this();
        this.product = product;
    }

    public String getId() {
        return product.getId();
    }

    @Override
    public String getTitle() {
        return product.getTitle();
    }

    @Override
    public void setTitle(String title) {
        product.setTitle(title);
    }

    @Override
    public BigDecimal getPrice() {
        return product.getPrice();
    }

    @Override
    public void setPrice(BigDecimal price) {
        product.setPrice(price);
    }

    @Override
    public String getDescription() {
        return product.getDescription();
    }

    @Override
    public void setDescription(String description) {
        product.setDescription(description);
    }

    @Override
    public String getSlug() {
        return product.getSlug();
    }

    @Override
    public void setSlug(String slug) {
        product.setSlug(slug);
    }

    @Override
    public Integer getStock() {
        return product.getStock();
    }

    @Override
    public void setStock(Integer stock) {
        product.setStock(stock);
    }

    @Override
    public List<String> getSizes() {
        return product.getSizes();
    }

    @Override
    public void setSizes(List<String> sizes) {
        product.setSizes(sizes);
    }

    @Override
    public String getGender() {
        return product.getGender();
    }

    @Override
    public void setGender(String gender) {
        product.setGender(gender);
    }

    @Override
    public List<String> getTags() {
        return product.getTags();
    }

    @Override
    public void setTags(List<String> tags) {
        product.setTags(tags);
    }

    @Override
    public String getType() {
        return product.getType();
    }

    @Override
    public void setType(String type) {
        product.setType(type);
    }

    @Override
    public List<T> getImages() {
        return images;
    }

    @Override
    public void setImages(List<T> images) {
        this.images = images;
    }
}
