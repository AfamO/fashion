package com.longbridge.dto;

import com.longbridge.models.Cart;

import java.util.List;

/**
 * Created by Longbridge on 22/03/2018.
 */
public class CartListDTO {
    private List<Cart> carts;

    public CartListDTO(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
