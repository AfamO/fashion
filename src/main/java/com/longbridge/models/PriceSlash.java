package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Created by Longbridge on 14/05/2018.
 */
@Entity
public class PriceSlash extends CommonFields{
    @OneToOne
    private Products products;

    private Double slashedPrice;

    private Double percentageDiscount;

    public PriceSlash() {
    }

    public PriceSlash(Products products, Double slashedPrice) {
        this.products = products;
        this.slashedPrice = slashedPrice;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Double getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(Double slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public Double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(Double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }
}
