package com.longbridge.repository;

import com.longbridge.models.Category;
import com.longbridge.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Longbridge on 13/11/2017.
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByDelFlagAndCategory(String delFlag,Category category);
    List<SubCategory> findBySubCategory(String subCategory);

    List<SubCategory> findByDelFlag(String delFlag);
    List<SubCategory> findByDelFlagAndCategoryAndProductType(String delFla, Category category, int ProductType);
}
