package com.teslo.teslo_shop.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.product.dto.PlainProductDto;
import com.teslo.teslo_shop.product.interfaces.ProductInterface;
import com.teslo.teslo_shop.product.product_image.ProductImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product implements ProductInterface<ProductImage> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    /**
     * @info
     * 
     *       Precision is the number of digits in a number.
     *       Scale is the number of digits to the right of the decimal point in a
     *       number.
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer stock = 0;

    private List<String> sizes = new ArrayList<>();

    private String gender;

    private List<String> tags = new ArrayList<>();

    private String type;

    @JsonManagedReference // two-way linkage between fields; its role is "parent" (or "forward") link.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ProductImage> images = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Specifies an on delete action for a foreign key constraint.
    private User user;

    public Product() {
    }

    public Product(PlainProductDto product, User user) {
        this();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.slug = product.getSlug();
        this.stock = product.getStock();
        this.sizes = product.getSizes();
        this.gender = product.getGender();
        this.tags = product.getTags();
        this.type = product.getType();
        this.images = product.getImages().stream().map(url -> new ProductImage(url, this)).collect(Collectors.toList());
        this.user = user;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public Integer getStock() {
        return stock;
    }

    @Override
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public List<String> getSizes() {
        return sizes;
    }

    @Override
    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<ProductImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public void addImages(List<ProductImage> images) {
        images.stream().forEach(image -> this.images.add(image));
    }

    public void subtractImages(List<ProductImage> images) {
        images.stream().forEach(image -> this.images.remove(image));
    }

    public void subtractImagesByUrls(List<String> urls) {
        List<ProductImage> images = this.images.stream()
                .filter(image -> urls.contains(image.getUrl())).collect(Collectors.toList());
        this.subtractImages(images);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
