package com.longbridge.models;


import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.OneToOne;

@Entity
public class ProductItem extends CommonFields implements Serializable {
    
    @OneToOne
    private Products products;
    
    private String  materialName;

    private int stockNo;

    private String inStock;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

}
