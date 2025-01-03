package com.teslo.teslo_shop.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.teslo.teslo_shop.product.dto.ProductDto;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductDto> getProducts() {
        List<Product> products = this.repository.findAll();
        return products.stream().map(product -> new ProductDto(product)).collect(Collectors.toList());
    }
}
