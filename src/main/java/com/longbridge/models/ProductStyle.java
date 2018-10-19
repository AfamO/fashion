/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longbridge.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author longbridge
 */
@Entity
public class ProductStyle extends CommonFields implements Serializable{
    
    @OneToOne
    private Products products;
    
    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<ProductPicture> picture;

    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<ProductColorStyles> productColorStyles;

    @OneToOne
    private Style style;

    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<ArtWorkPicture> artWorkPicture;

    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<MaterialPicture> materialPicture;
    
    public List<ArtWorkPicture> getArtWorkPicture() {
        return artWorkPicture;
    }

    public void setArtWorkPicture(List<ArtWorkPicture> artWorkPicture) {
        this.artWorkPicture = artWorkPicture;
    }

    public List<MaterialPicture> getMaterialPicture() {
        return materialPicture;
    }

    public void setMaterialPicture(List<MaterialPicture> materialPicture) {
        this.materialPicture = materialPicture;
    }
    
    public List<ProductPicture> getPicture() {
        return picture;
    }

    public void setPicture(List<ProductPicture> picture) {
        this.picture = picture;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public List<ProductColorStyles> getProductColorStyles() {
        return productColorStyles;
    }

    public void setProductColorStyles(List<ProductColorStyles> productColorStyles) {
        this.productColorStyles = productColorStyles;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    
}
