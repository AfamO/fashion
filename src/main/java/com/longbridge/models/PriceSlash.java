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
    private Products products;

    private double slashedPrice;

    private double percentageDiscount;

    public PriceSlash() {
    }


    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
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
}
