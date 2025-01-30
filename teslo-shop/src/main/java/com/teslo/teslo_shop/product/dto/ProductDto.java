package com.teslo.teslo_shop.product.dto;

import java.util.stream.Collectors;

import com.teslo.teslo_shop.product.Product;
import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

public class ProductDto extends BaseProductDto<ProductImageDto> {

    public ProductDto() {
        super();
    }

    public ProductDto(Product product) {
        super(product);
        super.setImages(product.getImages().stream().map(image -> new ProductImageDto(image))
                .collect(Collectors.toList()));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
