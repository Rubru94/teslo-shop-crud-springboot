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
public class Product {

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

    private List<String> sizes;

    private String gender;

    private List<String> tags;

    private String type;

    @JsonManagedReference // two-way linkage between fields; its role is "parent" (or "forward") link.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ProductImage> images = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // TODO: nullable false. It is true at the beginning for create
                                                   // column in table "products"
    @OnDelete(action = OnDeleteAction.CASCADE) // Specifies an on delete action for a foreign key constraint.
    private User user;

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

    public List<ProductImage> getImages() {
        return images;
    }

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
