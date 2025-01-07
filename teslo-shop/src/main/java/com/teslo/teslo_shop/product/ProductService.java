package com.teslo.teslo_shop.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.utils.StringUtil;
import com.teslo.teslo_shop.product.dto.ProductDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository repository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public List<ProductDto> findAll() {
        List<Product> products = this.repository.findAll();
        return products.stream().map(product -> this.mapToDto(product))
                .collect(Collectors.toList());
    }

    public ProductDto findOne(String term) {

        Product product = null;

        if (StringUtil.isUUID(term))
            product = this.repository.findById(term).orElse(null);
        else {
            String queryStr = "SELECT prod FROM Product prod " // LEFT JOIN FETCH prod.images LEFT JOIN FETCH prod.user
                    + "WHERE UPPER(prod.title) = :title OR prod.slug = :slug";
            TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
            query.setParameter("title", term.toUpperCase());
            query.setParameter("slug", term);
            product = query.getResultList().stream().findFirst().orElse(null);
        }

        if (product == null)
            throw new NotFoundException("Not found product with term(id, title or slug): " + term);

        return this.mapToDto(product);
    }

    public ProductDto create(Product product) {
        this.repository.save(product);
        return this.mapToDto(product);
    }

    private ProductDto mapToDto(Product product) {
        return objectMapper.convertValue(product, ProductDto.class);
    }
}
