package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longbridge on 10/18/17.
 */
@Indexed
@Entity
public class Products extends CommonFields implements Serializable {
    @Field
    private String name;

    private double amount;



    @IndexedEmbedded(depth = 1)
    @OneToOne
    private SubCategory subCategory;

    @JsonIgnore
    @ManyToOne
    private Designer designer;


    private String designerStatus="A";


    @Lob
    private String prodDesc;


    private String prodSummary;


   // public ArrayList<String> color;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ArtWorkPicture> artWorkPicture;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<MaterialPicture> materialPicture;

    private Double materialPrice;

    private String  materialName;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductPicture> picture;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductAttribute> productAttributes;



//    @OneToMany (mappedBy = "products")
//    public List<ProductSizes> productSizes;

    @OneToOne
    private Style style;


    private int stockNo;

    private String inStock;

    private String acceptCustomSizes;

    private String status = "A";

    private String verifiedFlag = "N";

    private String sponsoredFlag = "N";

    private String availability;

    private int productType;

    @Lob
    private String mandatoryMeasurements;

    private int numOfTimesOrdered = 0;

    @OneToMany(mappedBy = "products",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WishList> wishLists;


    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductRating> reviews;

    private boolean priceSlashEnabled = false;

    @JsonIgnore
    @OneToOne (mappedBy = "products", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PriceSlash priceSlash;

    private int numOfDaysToComplete;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getDesignerStatus() {
        return designerStatus;
    }

    public void setDesignerStatus(String designerStatus) {
        this.designerStatus = designerStatus;
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

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public List<ProductPicture> getPicture() {
        return picture;
    }

    public void setPicture(List<ProductPicture> picture) {
        this.picture = picture;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
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

    public String getAcceptCustomSizes() {
        return acceptCustomSizes;
    }

    public void setAcceptCustomSizes(String acceptCustomSizes) {
        this.acceptCustomSizes = acceptCustomSizes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getSponsoredFlag() {
        return sponsoredFlag;
    }

    public void setSponsoredFlag(String sponsoredFlag) {
        this.sponsoredFlag = sponsoredFlag;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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

    public boolean isPriceSlashEnabled() {
        return priceSlashEnabled;
    }

    public void setPriceSlashEnabled(boolean priceSlashEnabled) {
        this.priceSlashEnabled = priceSlashEnabled;
    }

    public PriceSlash getPriceSlash() {
        return priceSlash;
    }

    public void setPriceSlash(PriceSlash priceSlash) {
        this.priceSlash = priceSlash;
    }

    public int getNumOfDaysToComplete() {
        return numOfDaysToComplete;
    }

    public void setNumOfDaysToComplete(int numOfDaysToComplete) {
        this.numOfDaysToComplete = numOfDaysToComplete;
    }


    //    @Override
//    @JsonIgnore
//    public List<String> getDefaultSearchFields() {
//        return Arrays.asList("name");
//    }
}
