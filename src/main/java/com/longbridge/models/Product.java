package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;



@Entity
public class Product extends CommonFields implements Serializable {

    private String name;

    private String prodSummary;

    @Lob
    private String prodDesc;


    @OneToOne
    private SubCategory subCategory;


    @JsonIgnore
    @ManyToOne
    private Designer designer;



    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductStatuses productStatuses;
    

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductPrice productPrice;


    private int numOfTimesOrdered = 0;
    
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductStyle productStyle;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductColorStyle> productColorStyles;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WishList> wishLists;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRating> reviews;


    private int productType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
    
    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdSummary() {
        return prodSummary;
    }

    public void setProdSummary(String prodSummary) {
        this.prodSummary = prodSummary;
    }


    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }


    public int getNumOfTimesOrdered() {
        return numOfTimesOrdered;
    }

    public void setNumOfTimesOrdered(int numOfTimesOrdered) {
        this.numOfTimesOrdered = numOfTimesOrdered;
    }

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<WishList> wishLists) {
        this.wishLists = wishLists;
    }

    public List<ProductRating> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductRating> reviews) {
        this.reviews = reviews;
    }


    public ProductStyle getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = productStyle;
    }


    public ProductStatuses getProductStatuses() {
        return productStatuses;
    }

    public void setProductStatuses(ProductStatuses productStatuses) {
        this.productStatuses = productStatuses;
    }

    public List<ProductColorStyle> getProductColorStyles() {
        return productColorStyles;
    }

    public void setProductColorStyles(List<ProductColorStyle> productColorStyles) {
        this.productColorStyles = productColorStyles;
    }
}
