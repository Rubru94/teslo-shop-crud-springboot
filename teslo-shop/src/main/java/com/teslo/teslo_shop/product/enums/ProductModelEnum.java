package com.teslo.teslo_shop.product.enums;

public enum ProductModelEnum {
    MAIN("prod"),
    ID("id"),
    TITLE("title"),
    PRICE("price"),
    DESCRIPTION("description"),
    SLUG("slug"),
    STOCK("stock"),
    SIZES("sizes"),
    GENDER("gender"),
    TAGS("tags");

    private String str;

    ProductModelEnum(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
