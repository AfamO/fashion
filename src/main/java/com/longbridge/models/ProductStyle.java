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
    private Product product;
    
    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<ProductPicture> productPictures;

    @OneToMany(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private List<ProductColorStyle> productColorStyles;

    @OneToOne(mappedBy = "productStyle", cascade = CascadeType.ALL)
    private BespokeProduct bespokeProduct;

    @OneToOne
    private Style style;

    public List<ProductPicture> getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(List<ProductPicture> productPictures) {
        this.productPictures = productPictures;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public List<ProductColorStyle> getProductColorStyles() {
        return productColorStyles;
    }

    public void setProductColorStyles(List<ProductColorStyle> productColorStyles) {
        this.productColorStyles = productColorStyles;
    }

    public BespokeProduct getBespokeProduct() {
        return bespokeProduct;
    }

    public void setBespokeProduct(BespokeProduct bespokeProduct) {
        this.bespokeProduct = bespokeProduct;
    }
}
