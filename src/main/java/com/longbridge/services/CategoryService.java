package com.longbridge.services;

import com.longbridge.dto.SubCategoryDTO;
import com.longbridge.models.Category;
import com.longbridge.models.SubCategory;

import java.util.List;

/**
 * Created by Longbridge on 29/08/2018.
 */
public interface CategoryService {
    List<Category> getAllCategories();

    List<SubCategory> getSubCategories(Long categoryId);

    List<SubCategory> getAllSubCategories();

    List<SubCategory> getSubCategoriesByProductType(SubCategoryDTO subCategoryDTO);
}
