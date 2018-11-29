package com.longbridge.dto;

import com.longbridge.models.Cart;

public class PromoCodeApplyReqDTO {

    private String promoCode;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    private Cart cart;


    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }


}
