package com.longbridge.dto;

import java.util.List;

public class UserCartDTO {

    private Double totalPrice;
    List<CartDTO> cartItems;

    public UserCartDTO() {
    }

    public UserCartDTO(Double totalPrice, List<CartDTO> cartItems) {
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
