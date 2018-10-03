package com.longbridge.dto;

/**
 * Created by Longbridge on 13/12/2017.
 */
public class ProdSubCategoryDTO {
    private int page;

    private int size;

    private Long subcategoryId;

    private Long categoryId;

    private Long designerId;

    public ProdSubCategoryDTO() {
    }


    public ProdSubCategoryDTO(int page, int size, Long subcategoryId, Long categoryId, Long designerId) {
        this.page = page;
        this.size = size;
        this.subcategoryId = subcategoryId;
        this.categoryId = categoryId;
        this.designerId = designerId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }
}
