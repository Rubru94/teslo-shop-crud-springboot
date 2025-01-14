package com.teslo.teslo_shop.product.enums;

public enum ProductImageModelEnum {
    MAIN("prod_images"),
    ID("id"),
    URL("url"),
    ;

    private String str;

    ProductImageModelEnum(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
