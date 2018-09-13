package com.longbridge.repository;

import com.longbridge.models.Items;
import com.longbridge.models.OrderItemProcessingPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 13/09/2018.
 */
@Repository
public interface OrderItemProcessingPictureRepository  extends JpaRepository<OrderItemProcessingPicture, Long> {
    List<OrderItemProcessingPicture> findByItems(Items items);
}
