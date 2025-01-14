package com.teslo.teslo_shop.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.teslo.teslo_shop.product.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Product")
    void deleteAllProducts();
}
