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
public class Price extends CommonFields implements Serializable{

private double amount;

@OneToOne
private Products products;

@JsonIgnore
@OneToOne (mappedBy = "price", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private PriceSlash priceSlash;

private boolean priceSlashEnabled = false;

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
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
    
}
