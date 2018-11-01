/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 *
 * @author longbridge
 */
@Entity
public class ProductPrice extends CommonFields implements Serializable{

private double amount;

private double sewingAmount;

@JsonIgnore
@OneToOne
private Product product;

@JsonIgnore
@OneToOne (mappedBy = "productPrice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PriceSlash priceSlash;

    private boolean priceSlashEnabled = false;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PriceSlash getPriceSlash() {
        return priceSlash;
    }

    public void setPriceSlash(PriceSlash priceSlash) {
        this.priceSlash = priceSlash;
    }
    
    public boolean isPriceSlashEnabled() {
        return priceSlashEnabled;
    }

    public void setPriceSlashEnabled(boolean priceSlashEnabled) {
        this.priceSlashEnabled = priceSlashEnabled;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getSewingAmount() {
        return sewingAmount;
    }

    public void setSewingAmount(double sewingAmount) {
        this.sewingAmount = sewingAmount;
    }
}
