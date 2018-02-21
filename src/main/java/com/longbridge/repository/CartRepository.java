package com.longbridge.repository;

import com.longbridge.models.Address;
import com.longbridge.models.Cart;
import com.longbridge.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 18/01/2018.
 */
@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart,Long> {
    Long countByUser(User user);

    List<Cart> findByUser(User user);
}
