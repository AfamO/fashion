package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by Longbridge on 14/05/2018.
 */
@Entity
public class PriceSlash extends CommonFields implements Serializable{
    @OneToOne
    private ProductPrice productPrice;

    private double slashedPrice;

    private double percentageDiscount;

    public PriceSlash() {
    }

    public double getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(double slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }
}
