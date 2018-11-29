package com.longbridge.repository;

import com.longbridge.models.Address;
import com.longbridge.models.PromoCode;
import com.longbridge.models.PromoCodeUserStatus;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by longbridge on 11/5/17.
 */
@Repository
public interface PromoCodeUserStatusRepository extends JpaRepository<PromoCodeUserStatus,Long> {

    PromoCodeUserStatus findByUserAndPromoCode (User user, PromoCode promoCode);
    List<PromoCode> findByPromoCode(PromoCode promoCode);

    List<PromoCodeUserStatus> findByUser(User user);

    List<PromoCodeUserStatus> findByIsPromoCodeUsedByUser(String isPromoCodeUsedByUse);


}
