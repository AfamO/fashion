package com.longbridge.services;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.WishListDTO;
import com.longbridge.models.User;

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
