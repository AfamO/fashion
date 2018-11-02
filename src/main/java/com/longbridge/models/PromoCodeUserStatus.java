package com.longbridge.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;

@Entity
public class PromoCodeUserStatus extends CommonFields implements Serializable {

    private String  isPromoCodeUsedByUser="N";

    @OneToOne
    private PromoCode promoCode;

    @OneToOne
    private User user;

    private Long productId;

    private Double discountedAmount;

    public String getIsPromoCodeUsedByUser() {
        return isPromoCodeUsedByUser;
    }

    public void setIsPromoCodeUsedByUser(String isPromoCodeUsedByUser) {
        this.isPromoCodeUsedByUser = isPromoCodeUsedByUser;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(Double discountedAmount) {
        this.discountedAmount = discountedAmount;
    }
}
