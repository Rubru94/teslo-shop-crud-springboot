package com.teslo.teslo_shop.product.product_image;

import java.util.List;
import java.util.Optional;

import javax.swing.Spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * @see
     * 
     *      Spring Data JPA automatically understands the query methods based on the
     *      naming convention of the methods. In the case of findByProductId, Spring
     *      Data
     *      JPA infers that you want to find all ProductImages that have a product
     *      property with a specific id. Therefore, you don't need to implement the
     *      method manually.
     * 
     */
    /*
     * Implementation using @Query
     * 
     * @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
     * List<ProductImage> findByProductId(@Param("productId") String productId);
     */
    Optional<List<ProductImage>> findByProductId(String id);

    Optional<List<ProductImage>> findByProductIdAndUrlIn(String id, List<String> urls);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.id IN :ids")
    Optional<List<ProductImage>> findAllById(@Param("ids") List<Long> ids);

}
