package com.longbridge.repository;

import com.longbridge.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Repository
public interface PromoItemsRepository extends JpaRepository<PromoItem,Long> {
    List<PromoItem> findByItemIdAndItemType(Long itemId, String itemType);

    List<PromoItem> findByItemType(String itemType);

    PromoItem findByItemType(Long itemId, String itemType);


    @Query("Select pI from PromoItem pI  where pI.itemId=:itemId AND (pI.itemType =:itemTypeP OR pI.itemType =:itemTypeC ) ")
    List<PromoItem> findAllPromoItemsBelongToCategoryAndProduct(@Param("itemId") Long itemId,@Param("itemTypeP") String itemTypeP,@Param("itemTypeC")String itemTypeC);


}
