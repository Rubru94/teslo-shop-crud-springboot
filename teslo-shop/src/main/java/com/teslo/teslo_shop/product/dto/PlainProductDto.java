package com.teslo.teslo_shop.product.dto;

import java.util.stream.Collectors;

import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

public class PlainProductDto extends BaseProductDto<String> {

    /**
     * @see
     *      · Jackson needs a default constructor for instantiating the class
     */
    public PlainProductDto() {
        super();
    }

    public PlainProductDto(ProductDto productDto) {
        super(productDto.getProduct());
        /**
         * notation {@code ProductImageDto::getUrl} is called "method reference" in Java
         */
        super.setImages(productDto.getImages().stream().map(ProductImageDto::getUrl).collect(Collectors.toList()));
    }
}
