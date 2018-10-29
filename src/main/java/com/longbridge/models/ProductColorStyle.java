package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Longbridge on 15/08/2018.
 */
@Entity
public class ProductColorStyle extends CommonFields implements Serializable {

    private String colourPicture;
    private String colourName;


    @OneToMany(mappedBy = "productColorStyle", cascade = CascadeType.ALL)
    private List<ProductPicture> productPictures;

    @OneToMany(mappedBy = "productColorStyle", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductSizes> productSizes;

    private int stockNo;

    private String inStock;

    @JsonIgnore
    @ManyToOne
    private ProductStyle productStyle;

    @JsonIgnore
    @ManyToOne
    private Product product;


    public List<ProductPicture> getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(List<ProductPicture> productPictures) {
        this.productPictures = productPictures;
    }

    public String getColourPicture() {
        return colourPicture;
    }

    public void setColourPicture(String colourPicture) {
        this.colourPicture = colourPicture;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public List<ProductSizes> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSizes> productSizes) {
        this.productSizes = productSizes;
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

    public ProductStyle getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = productStyle;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

//    public BespokeProduct getBespokeProduct() {
//        return bespokeProduct;
//    }
//
//    public void setBespokeProduct(BespokeProduct bespokeProduct) {
//        this.bespokeProduct = bespokeProduct;
//    }
}

