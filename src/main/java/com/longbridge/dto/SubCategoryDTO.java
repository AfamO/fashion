package com.longbridge.dto;

import java.util.List;

/**
 * Created by Longbridge on 13/11/2017.
 */
public class SubCategoryDTO {
    public Long categoryId;

    public List<String> subCategoryName;

    public  Long id;

    public String name;

    public int productType;

    public SubCategoryDTO(Long categoryId, List<String> subCategoryName) {
        this.categoryId = categoryId;
        this.subCategoryName = subCategoryName;
    }

    public SubCategoryDTO() {
    }
}
