package com.longbridge.dto;

import com.longbridge.models.Cart;

public class PromoCodeApplyReqDTO {

    private String promoCode;

    private Long productId;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
