package com.longbridge.repository;

import com.longbridge.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 13/11/2017.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


}
