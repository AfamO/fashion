package com.longbridge.repository;

import com.longbridge.models.Designer;
import com.longbridge.models.Products;
import com.longbridge.models.SubCategory;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public interface ProductRepository extends JpaRepository<Products,Long> {
    List<Products> findByDesigner(Designer designer);
    Page<Products> findBySubCategory(Pageable pageable, SubCategory subCategory);
    Page<Products> findByDesignerAndSubCategory(Pageable pageable, Designer designer, SubCategory subCategory);

    List<Products> findFirst8ByDesigner(Designer designer);

    List<Products> findTop10ByOrderByNumOfTimesOrderedDesc();

   // @Query("select p from Products p where p.picture like %:pictureName%")
    //Products findByPicture(String pictureName);

    @Query(value = "select p.name from Products p where p.id =:id", nativeQuery = true)
    String getProductName(@Param("id") Long id);
}
