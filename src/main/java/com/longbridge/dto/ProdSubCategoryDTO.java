package com.longbridge.dto;

/**
 * Created by Longbridge on 13/12/2017.
 */
public class ProdSubCategoryDTO {
    public String page;

    public String size;

    public Long subcategoryId;

    public Long designerId;

    public ProdSubCategoryDTO() {
    }


    public ProdSubCategoryDTO(String page, String size, Long subcategoryId, Long designerId) {
        this.page = page;
        this.size = size;
        this.subcategoryId = subcategoryId;
        this.designerId = designerId;
    }

}
