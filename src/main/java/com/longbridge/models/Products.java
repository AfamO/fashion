package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Indexed
@Entity
public class Products extends CommonFields implements Serializable {
    @Field
    private String name;

    @IndexedEmbedded(depth = 1)
    @OneToOne
    private SubCategory subCategory;
    
    @OneToOne
    private ProductItem productItem;

    @JsonIgnore
    @ManyToOne
    private Designer designer;

    @Lob
    private String prodDesc;
    
    @OneToOne
    private ProductStatuses productStatuses;
    
    private String prodSummary;

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
    }

    private int productType;

    @OneToOne
    private ProductPrice price;

    @Lob
    private String mandatoryMeasurements;

    private int numOfTimesOrdered = 0;
    
    @OneToOne
    private ProductStyle productStyle;
    
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductColorStyles> productColorStyles;

    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WishList> wishLists;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductRating> reviews;

    private int numOfDaysToComplete;

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

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getMandatoryMeasurements() {
        return mandatoryMeasurements;
    }

    public void setMandatoryMeasurements(String mandatoryMeasurements) {
        this.mandatoryMeasurements = mandatoryMeasurements;
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

    public int getNumOfDaysToComplete() {
        return numOfDaysToComplete;
    }

    public void setNumOfDaysToComplete(int numOfDaysToComplete) {
        this.numOfDaysToComplete = numOfDaysToComplete;
    }
    public ProductStyle getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = productStyle;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public ProductStatuses getProductStatuses() {
        return productStatuses;
    }

    public void setProductStatuses(ProductStatuses productStatuses) {
        this.productStatuses = productStatuses;
    }

    public List<ProductColorStyles> getProductColorStyles() {
        return productColorStyles;
    }

    public void setProductColorStyles(List<ProductColorStyles> productColorStyles) {
        this.productColorStyles = productColorStyles;
    }
}
