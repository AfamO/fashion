package com.longbridge.dto;

/**
 * Created by Longbridge on 23/04/2018.
 */
public class FilterProductDTO {

    private int page;

    private int size;

    private String fromPrice;

    private String toPrice;

    private int productQualityRating;

    private String productName;

    private Long subCategoryId;

    public FilterProductDTO() {
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public String getToPrice() {
        return toPrice;
    }

    public void setToPrice(String toPrice) {
        this.toPrice = toPrice;
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

    public int getProductQualityRating() {
        return productQualityRating;
    }

    public void setProductQualityRating(int productQualityRating) {
        this.productQualityRating = productQualityRating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
