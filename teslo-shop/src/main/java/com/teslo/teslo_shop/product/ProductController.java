package com.teslo.teslo_shop.product;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teslo.teslo_shop.product.dto.ProductDto;

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

    @GetMapping("/{term}")
    public ProductDto findOne(@PathVariable(name = "term") String term) {
        return this.service.findOne(term);
    }

    @PostMapping()
    public ProductDto create(@RequestBody Product entity) {
        return this.service.save(entity);
    }

    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable String id, @RequestBody Product entity) throws BadRequestException {
        return this.service.update(id, entity);
    }

}
