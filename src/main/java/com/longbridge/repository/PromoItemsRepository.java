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
    List<PromoItem> findByItemIdAndItemType(PromoItem promoItem, String itemType);

    List<PromoItem> findByItemType(String itemType);

}
