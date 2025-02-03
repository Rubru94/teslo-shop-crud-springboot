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

import com.teslo.teslo_shop.auth.AuthService;
import com.teslo.teslo_shop.auth.enums.ValidRoles;
import com.teslo.teslo_shop.core.dto.PaginationDto;
import com.teslo.teslo_shop.product.dto.PlainProductDto;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final AuthService authService;

    public ProductController(ProductService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
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

    /**
     * {@code @PreAuthorize} notation implies using SpEL expressions which cannot be
     * built dynamically with the role string.
     * 
     * For this reason it is decided to use a custom implementation with an
     * auxiliary method {@code verifyRoles} to check user roles.
     */
    // @PreAuthorize("hasAuthority('admin')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PlainProductDto create(@RequestBody PlainProductDto entity) {
        this.authService.verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        return this.service.save(entity, this.authService.getJwtUser());
    }

    @PatchMapping("/{id}")
    public PlainProductDto update(@PathVariable String id, @RequestBody PlainProductDto entity)
            throws BadRequestException {
        this.authService.verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        return this.service.update(id, entity);
    }

    @DeleteMapping("{id}")
    public PlainProductDto delete(@PathVariable String id) {
        this.authService.verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        return this.service.delete(id);
    }

}
