package com.longbridge.repository;

import com.longbridge.models.Category;
import com.longbridge.models.SubCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Longbridge on 13/11/2017.
 */
public interface SubCategoryRepository extends PagingAndSortingRepository<SubCategory, Long>{
    List<SubCategory> findByCategory(Category category);
}
