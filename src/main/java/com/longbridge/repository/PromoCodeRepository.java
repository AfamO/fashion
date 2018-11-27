package com.longbridge.repository;

import com.longbridge.models.Address;
import com.longbridge.models.PromoCode;
import com.longbridge.models.PromoItem;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 11/5/17.
 */
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {
    List<PromoCode> findByCodeAndValue(String code, String value);

    List<PromoCode> findByIsUsedStatusNot(String status);
    @Query("Select pC from PromoCode pC  where pC.code =:code AND pC.expiryDate > CURRENT_TIMESTAMP ")
    PromoCode findUniqueUnExpiredPromoCode(@Param("code") String code);
    
    @Query("Select pC from PromoCode pC  where pC.code =:code AND pC.expiryDate > CURRENT_TIMESTAMP AND pC.verifiedFlag =:verifiedFlag ")
    PromoCode findUniqueUnExpiredAndVerifiedPromoCode(@Param("code") String code,@Param("verifiedFlag")String verifiedFlag);

    @Query("select p from PromoCode p where p.promoItems in :myPromoItems")
    PromoCode findByPromoItems(@Param("myPromoItems") List<PromoItem> promoItems);

    List<PromoCode> findByExpiryDate(String expiryDate);

    @Query("Select pC from PromoCode pC  where pC.expiryDate< CURRENT_TIMESTAMP order by pC.createdOn desc ")
    List<PromoCode> findExpiredPromoCodes();

    @Query("Select pC from PromoCode pC  where pC.expiryDate > CURRENT_TIMESTAMP order by pC.createdOn desc ")
    List<PromoCode> findUnExpiredPromoCodes();

}
