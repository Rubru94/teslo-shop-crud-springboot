package com.teslo.teslo_shop.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.product.dto.ProductDto;

@Service
public class ProductService {

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

    public ProductDto create(Product product) {
        this.repository.save(product);
        return this.mapToDto(product);
    }

    private ProductDto mapToDto(Product product) {
        return objectMapper.convertValue(product, ProductDto.class);
    }
}
