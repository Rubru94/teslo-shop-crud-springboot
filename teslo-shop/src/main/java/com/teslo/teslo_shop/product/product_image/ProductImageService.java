package com.teslo.teslo_shop.product.product_image;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.product.Product;
import com.teslo.teslo_shop.product.ProductRepository;
import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

@Transactional
@Service
public class ProductImageService {

    private final ProductImageRepository repository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductImageService(ProductImageRepository repository, ProductRepository productRepository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public List<ProductImageDto> findAllByIds(List<Long> ids) {
        List<ProductImage> productImages = this.repository.findAllById(ids)
                .orElseThrow(() -> new NotFoundException("Not found all product images with these ids: " + ids));
        return productImages.stream().map(image -> this.mapToDto(image)).collect(Collectors.toList());
    }

    public ProductImageDto findOne(Long id) {
        ProductImage productImage = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product image with id: " + id));
        return this.mapToDto(productImage);
    }

    public List<ProductImageDto> findByProductId(String id) {
        List<ProductImage> productImages = this.repository.findByProductId(id)
                .orElseThrow(() -> new NotFoundException("Not found product images with product id: " + id));
        return productImages.stream().map(image -> this.mapToDto(image)).collect(Collectors.toList());
    }

    public List<ProductImageDto> findByProductIdAndUrlIn(String id, List<String> urls) {
        List<ProductImage> productImages = this.repository.findByProductIdAndUrlIn(id, urls)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Not found product images with urls: " + urls + " & product id: " + id));
        return productImages.stream().map(image -> this.mapToDto(image)).collect(Collectors.toList());
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

    public ProductImageDto delete(Long id) {
        ProductImageDto productImageDto = this.findOne(id);
        this.repository.delete(this.mapToEntity(productImageDto));
        return productImageDto;
    }

    public List<ProductImageDto> deleteMultiple(List<Long> ids) {
        List<ProductImageDto> productImageDtos = this.findAllByIds(ids);
        this.repository
                .deleteAll(
                        productImageDtos.stream().map(image -> this.mapToEntity(image)).collect(Collectors.toList()));
        return productImageDtos;
    }

    public List<ProductImageDto> deleteByProductIdAndUrlIn(String id, List<String> urls) {
        List<ProductImageDto> productImageDtos = this.findByProductIdAndUrlIn(id, urls);
        List<ProductImage> productImages = productImageDtos.stream().map(image -> this.mapToEntity(image))
                .collect(Collectors.toList());
        this.repository.deleteAll(productImages);
        return productImageDtos;
    }

    private ProductImageDto mapToDto(ProductImage image) {
        return new ProductImageDto(image);
    }

    private ProductImage mapToEntity(ProductImageDto image) {
        ProductImage productImage = objectMapper.convertValue(image, ProductImage.class);
        Product product = productRepository.findById(image.getProductId()).orElse(null);
        productImage.setProduct(product);
        return productImage;
    }
}
