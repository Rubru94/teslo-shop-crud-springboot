package com.teslo.teslo_shop.product.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface ProductInterface<T> {
    public String getTitle();

    public void setTitle(String title);

    public BigDecimal getPrice();

    public void setPrice(BigDecimal price);

    public String getDescription();

    public void setDescription(String description);

    public String getSlug();

    public void setSlug(String slug);

    public Integer getStock();

    public void setStock(Integer stock);

    public List<String> getSizes();

    public void setSizes(List<String> sizes);

    public String getGender();

    public void setGender(String gender);

    public List<String> getTags();

    public void setTags(List<String> tags);

    public String getType();

    public void setType(String type);

    public List<T> getImages();

    public void setImages(List<T> images);
}
