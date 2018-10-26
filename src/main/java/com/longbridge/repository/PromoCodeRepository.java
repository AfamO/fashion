package com.longbridge.repository;

import com.longbridge.models.Address;
import com.longbridge.models.PromoCode;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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

    PromoCode findByCode(String code);

    List<PromoCode> findByExpiryDate(String expiryDate);

    @Query("Select pC from PromoCode pC  where pC.expiryDate< CURRENT_DATE ")
    List<PromoCode> findExpiredPromoCodes();

    @Query("Select pC from PromoCode pC  where pC.expiryDate > CURRENT_DATE ")
    List<PromoCode> findUnExpiredPromoCodes();

}
