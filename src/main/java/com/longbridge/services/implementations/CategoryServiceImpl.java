package com.longbridge.services.implementations;

import com.longbridge.dto.SubCategoryDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Category;
import com.longbridge.models.SubCategory;
import com.longbridge.repository.CategoryRepository;
import com.longbridge.repository.SubCategoryRepository;
import com.longbridge.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 29/08/2018.
 */
@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Override
    public List<Category> getAllCategories() {

        try {
            List<Category> categories = categoryRepository.findAll();
            return categories;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<SubCategory> getSubCategories(Long categoryId) {
        List<SubCategory> subCategories = new ArrayList<>();
        try {
            Category category = categoryRepository.findOne(categoryId);
            if(category != null) {
                subCategories= subCategoryRepository.findByDelFlagAndCategory("N",category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
        return subCategories;
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        try {
            return subCategoryRepository.findByDelFlag("N");
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public List<SubCategory> getSubCategoriesByProductType(SubCategoryDTO subCategoryDTO) {
        List<SubCategory> subCategories = new ArrayList<>();
        try {
            Category category = categoryRepository.findOne(subCategoryDTO.categoryId);
            if(category != null) {
                subCategories = subCategoryRepository.findByDelFlagAndCategoryAndProductType("N",category, subCategoryDTO.productType);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
        return subCategories;
    }
}
