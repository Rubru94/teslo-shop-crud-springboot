package com.teslo.teslo_shop.core.dto;

public class PaginationDto {

    private Integer limit;
    private Integer offset;

    public PaginationDto() {
        this.limit = 10;
        this.offset = 0;
    }

    public PaginationDto(PaginationDto paginationDto) {
        this();
        if (paginationDto != null) {
            this.limit = paginationDto.getLimit();
            this.offset = paginationDto.getOffset();
        }
    }

    public PaginationDto(Integer limit) {
        this();
        this.limit = limit;
    }

    public PaginationDto(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
