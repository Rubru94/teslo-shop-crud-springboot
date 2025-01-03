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

    public List<ProductDto> getProducts() {
        List<Product> products = this.repository.findAll();
        return products.stream().map(product -> objectMapper.convertValue(product, ProductDto.class))
                .collect(Collectors.toList());
    }
}
