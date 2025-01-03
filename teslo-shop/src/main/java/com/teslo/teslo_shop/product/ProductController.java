package com.teslo.teslo_shop.product;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teslo.teslo_shop.product.dto.ProductDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping()
    public List<ProductDto> findAll() {
        return this.service.findAll();
    }

    @PostMapping()
    public ProductDto create(@RequestBody Product entity) {
        return this.service.create(entity);
    }

}
