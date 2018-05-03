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
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {
    List<Products> findByDesignerAndVerifiedFlag(Designer designer, String flag);
    List<Products> findByDesigner(Designer designer);
    Page<Products> findBySubCategoryAndVerifiedFlag(Pageable pageable, SubCategory subCategory,String flag);
    List<Products> findFirst9BySubCategoryAndVerifiedFlag(SubCategory subCategory,String flag);
    List<Products> findFirst10BySubCategoryAndVerifiedFlag(SubCategory subCategory,String flag);
    Page<Products> findByDesignerAndSubCategoryAndVerifiedFlag(Pageable pageable, Designer designer, SubCategory subCategory, String flag);
    int countByDesigner(Designer designer);
    List<Products> findFirst8ByDesignerAndVerifiedFlag(Designer designer,String flag);

    List<Products> findTop10ByDesignerStatusOrderByNumOfTimesOrderedDesc(String designerStatus);
    Page<Products> findByVerfiedOnIsNull(Pageable pageable);
   // @Query("select p from Products p where p.picture like %:pictureName%")
    //Products findByPicture(String pictureName);

    @Query(value = "select p.name from Products p where p.id =:id", nativeQuery = true)
    String getProductName(@Param("id") Long id);

    Page<Products> findByVerifiedFlagAndAmountBetween(String verifiedFlag, Double fromAmount, Double toAmount, Pageable pageable);

    Page<Products> findByVerifiedFlagAndNameLikeAndAmountBetween(String verifiedFlag, String name, Double fromAmount, Double toAmount, Pageable pageable);

    Page<Products> findByVerifiedFlagAndNameLike(String verifiedFlag, String name,Pageable pageable);

    //Page<Products> findByVerifiedFlagAndNameLikeAndp(String verifiedFlag, String name, Double fromAmount, Double toAmount, Pageable pageable);


}
