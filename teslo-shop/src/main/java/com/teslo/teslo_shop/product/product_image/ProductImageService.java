package com.teslo.teslo_shop.product.product_image;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

@Service
public class ProductImageService {

    private final ProductImageRepository repository;
    private final ObjectMapper objectMapper;

    public ProductImageService(ProductImageRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public ProductImageDto save(ProductImage image) {
        ProductImage savedProductImage = this.repository.save(image);
        return this.mapToDto(savedProductImage);
    }

    public List<ProductImageDto> saveMultiple(List<ProductImage> productImages) {
        List<ProductImage> savedImages = this.repository.saveAll(productImages);
        return savedImages.stream().map(image -> this.mapToDto(image))
                .collect(Collectors.toList());
    }

    private ProductImageDto mapToDto(ProductImage image) {
        return objectMapper.convertValue(image, ProductImageDto.class);
    }
}
