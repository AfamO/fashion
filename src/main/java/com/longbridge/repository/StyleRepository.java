package com.longbridge.repository;

import com.longbridge.models.Style;
import com.longbridge.models.SubCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Longbridge on 13/11/2017.
 */
public interface StyleRepository extends PagingAndSortingRepository<Style, Long>{
        List<Style> findBySubCategory(SubCategory subCategory);
}
