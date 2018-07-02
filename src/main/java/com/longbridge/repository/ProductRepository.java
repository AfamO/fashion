package com.longbridge.repository;

import com.longbridge.dto.ProductsWithRating;
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

    Page<Products> findBySubCategoryAndVerifiedFlagAndDesigner_Status(Pageable pageable, SubCategory subCategory,String flag,String status);

    List<Products> findFirst9BySubCategoryAndSponsoredFlagAndVerifiedFlag(SubCategory subCategory,String sponsoredFlag,String flag);
    List<Products> findFirst9BySubCategoryAndVerifiedFlag(SubCategory subCategory,String flag);
    List<Products> findFirst10BySubCategoryAndVerifiedFlag(SubCategory subCategory,String flag);
    List<Products> findFirst10BySubCategoryAndSponsoredFlagAndVerifiedFlag(SubCategory subCategory,String sponsoredFlag,String flag);
    Page<Products> findByDesignerAndSubCategoryAndVerifiedFlag(Pageable pageable, Designer designer, SubCategory subCategory, String flag);
    int countByDesigner(Designer designer);
    List<Products> findFirst8ByDesignerAndVerifiedFlag(Designer designer,String flag);

    List<Products> findFirst5BySponsoredFlag(String flag);

    List<Products> findFirst5ByPriceSlashEnabledTrue();

    List<Products> findTop10ByDesignerStatusAndNumOfTimesOrderedNotOrderByNumOfTimesOrderedDesc(String designerStatus,int no);

    Page<Products> findByVerfiedOnIsNull(Pageable pageable);
   // @Query("select p from Products p where p.picture like %:pictureName%")
    //Products findByPicture(String pictureName);

    @Query(value = "select p.name from Products p where p.id =:id", nativeQuery = true)
    String getProductName(@Param("id") Long id);


    @Query(value = "select p.mandatory_measurements from Products p where p.id =:id", nativeQuery = true)
    String getMandatoryMeasurements(@Param("id") Long id);

    Page<Products> findByVerifiedFlagAndDesignerStatusAndAmountBetween(String verifiedFlag, String designerStatus, Double fromAmount, Double toAmount, Pageable pageable);

    List<Products> findByIdIn(List<Long> ids);

    //Page<Products> findByVerifiedFlagAndNameLikeAndAmountBetween(String verifiedFlag, String name, Double fromAmount, Double toAmount, Pageable pageable);

    List<Products> findByVerifiedFlagAndDesignerStatus(String verifiedFlag, String designerStatus);

    //List<Products> findByVerifiedFlagAndDesignerStatusAndNameIsLike(String verifiedFlag, String designerStatus, String name);

    @Query(value = "select p.id from Products p where p.verifiedFlag='Y' and p.designerStatus='A' and p.name like %:name%")
    List<Long> findByVerifiedFlagAndDesignerStatusAndNameIsLike(@Param("name") String name);

    //List<Products> findByVerifiedFlagAndDesignerStatusAndAmountBetween(String verifiedFlag, String designerStatus, Double fromAmount, Double toAmount);

    @Query(value = "select p.id from Products p where p.verifiedFlag='Y' and p.designerStatus='A' and (p.amount >= :fromAmount and p.amount <= :toAmount)")
    List<Long> findByVerifiedFlagAndDesignerStatusAndAmountBetween(@Param("fromAmount") double fromAmount, @Param("toAmount") double toAmount);


    Page<Products> findByVerifiedFlagAndNameLike(String verifiedFlag, String name,Pageable pageable);

    List<Products> findByVerifiedFlagAndNameLike(String verifiedFlag, String name);

    //Page<Products> findByVerifiedFlagAndNameLikeAndp(String verifiedFlag, String name, Double fromAmount, Double toAmount, Pageable pageable);

//    @Query(value = "select p.*, sum(pr.product_quality_rating) as rating FROM products p INNER JOIN product_rating pr WHERE p.id = pr.products_id ORDER by rating desc Limit 0, 10", nativeQuery = true)
//    List<ProductsWithRating> findTop10FrequentlyBoughtProducts();

}
