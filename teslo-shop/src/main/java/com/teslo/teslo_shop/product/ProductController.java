package com.teslo.teslo_shop.product;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.teslo.teslo_shop.core.dto.PaginationDto;
import com.teslo.teslo_shop.product.dto.PlainProductDto;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /*
     * if we use @RequestParam instead of @ModelAttribute
     * 
     * @RequestParam(required = false) Integer limit,
     * 
     * @RequestParam(required = false) Integer offset
     */
    @GetMapping()
    public List<PlainProductDto> findAll(@ModelAttribute PaginationDto paginationDto) {
        return this.service.findAll(paginationDto);
    }

    @GetMapping("/{term}")
    public PlainProductDto findOne(@PathVariable(name = "term") String term) {
        return this.service.findOne(term);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PlainProductDto create(@RequestBody PlainProductDto entity) {
        return this.service.save(entity);
    }

    @PatchMapping("/{id}")
    public PlainProductDto update(@PathVariable String id, @RequestBody PlainProductDto entity)
            throws BadRequestException {
        return this.service.update(id, entity);
    }

    @DeleteMapping("{id}")
    public PlainProductDto delete(@PathVariable String id) {
        return this.service.delete(id);
    }

}
