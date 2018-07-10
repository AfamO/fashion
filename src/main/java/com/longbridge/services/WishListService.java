package com.longbridge.services;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.WishListDTO;
import com.longbridge.models.Products;
import com.longbridge.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Longbridge on 19/01/2018.
 */

public interface WishListService {
    String addToWishList(WishListDTO wishListDTO, User user);

    String notifyMe(WishListDTO wishListDTO, User user);

    List<WishListDTO> getWishLists(User user, PageableDetailsDTO pageable);

    void deleteWishList(Long id);

}
