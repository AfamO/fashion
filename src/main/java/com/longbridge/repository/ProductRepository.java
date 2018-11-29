package com.longbridge.repository;

import com.longbridge.models.Category;
import com.longbridge.models.Designer;
import com.longbridge.models.Product;
import com.longbridge.models.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByDesignerAndProductStatuses_VerifiedFlag(Designer designer, String flag);
    List<Product> findByDesigner(Designer designer);
    Page<Product> findBySubCategoryAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(Pageable pageable, SubCategory subCategory, String flag, String status);
    Page<Product> findBySubCategory_CategoryAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(Pageable pageable, Category category, String flag, String status);
    List<Product> findFirst9BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(SubCategory subCategory, String sponsoredFlag, String flag);
    List<Product> findFirst10BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(SubCategory subCategory, String sponsoredFlag, String flag);
    Page<Product> findByDesignerAndSubCategoryAndProductStatuses_VerifiedFlag(Pageable pageable, Designer designer, SubCategory subCategory, String flag);
    int countByDesigner(Designer designer);
    List<Product> findFirst8ByDesignerAndProductStatuses_VerifiedFlag(Designer designer, String flag);
    List<Product> findByProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(String sponsoredFlag, String verifiedFlag, String status);
    Page<Product> findByProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(Pageable pageable, String sponsoredFlag, String verifiedFlag, String status);
   // List<Product> findFirst5ByPriceSlashEnabledTrue();
    Long countByProductStatuses_VerifiedFlag(String flag);
    List<Product> findTop10ByProductStatuses_DesignerStatusAndNumOfTimesOrderedNotOrderByNumOfTimesOrderedDesc(String designerStatus, int no);

    Page<Product> findByProductStatuses_SponsoredFlag(Pageable pageable, String sponsoredFlag);

    List<Product> findByProductStatuses_SponsoredFlag(String sponsoredFlag);


    Page<Product> findByProductStatuses_VerifiedFlagOrderByCreatedOnDesc(Pageable pageable);

    Page<Product> findByProductStatuses_VerifiedFlagAndProductStatuses_DelFlagAndCreatedOnBetween
            (String  verifiedFlg,String delFlg,Date startDate, Date endDate, Pageable pageable);


    Page<Product> findByProductStatuses_VerifiedFlag(String verifiedFlag, Pageable pageable);

    @Query(value = "select p.name from Products p where p.id =:id", nativeQuery = true)
    String getProductName(@Param("id") Long id);


    @Query(value = "select p.mandatory_measurements from Products p where p.id =:id", nativeQuery = true)
    String getMandatoryMeasurements(@Param("id") Long id);

    Page<Product> findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndProductPrice_AmountBetween(String verifiedFlag, String designerStatus, Double fromAmount, Double toAmount, Pageable pageable);

    List<Product> findByIdIn(List<Long> ids);



    Page<Product> findByProductStatuses_VerifiedFlagAndNameLike(String verifiedFlag, String name, Pageable pageable);

    List<Product> findByProductStatuses_VerifiedFlagAndNameLike(String verifiedFlag, String name);

//    @Query(value = "select p.*, sum(pr.product_quality_rating) as rating FROM products p INNER JOIN product_rating pr WHERE p.id = pr.products_id ORDER by rating desc Limit 0, 10", nativeQuery = true)
//    List<ProductsWithRating> findTop10FrequentlyBoughtProducts();

//    @Query(value = "select p.id from Product p where p.verifiedFlag='Y' and p.designerStatus='A' and (p.amount >= :fromAmount and p.amount <= :toAmount) and p.subCategory=:subCategory ")
//    List<Long> filterProductByPrice(@Param("fromAmount") double fromAmount, @Param("toAmount") double toAmount, @Param("subCategory") SubCategory subCategory);


    List<Product>findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndProductPrice_AmountBetweenAndSubCategory(String verifiedFlag, String designerStatus,double fromAmount, double toAmount,SubCategory subCategory);

    List<Product> findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndNameLike(String verifiedFlag, String designerStatus,String name);

    List<Product> findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndSubCategory(String verifiedFlag, String designerStatus, SubCategory subCategory);
}
