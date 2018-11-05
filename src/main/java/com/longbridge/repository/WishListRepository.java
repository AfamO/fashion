package com.longbridge.repository;

import com.longbridge.models.Product;
import com.longbridge.models.User;
import com.longbridge.models.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 19/01/2018.
 */
@Repository
public interface WishListRepository extends PagingAndSortingRepository<WishList,Long>{
    Long countByUser(User user);
    Page<WishList> findByUser(User user, Pageable pageable);
    WishList findByUserAndProduct(User user,Product product);
}
