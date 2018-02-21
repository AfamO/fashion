package com.longbridge.dto;

import com.longbridge.models.Category;

import java.util.List;

/**
 * Created by Longbridge on 13/11/2017.
 */
public class SubCategoryDTO {
    public String categoryId;

    public List<String> subCategoryName;

    public SubCategoryDTO(String categoryId, List<String> subCategoryName) {
        this.categoryId = categoryId;
        this.subCategoryName = subCategoryName;
    }

    public SubCategoryDTO() {
    }
}
